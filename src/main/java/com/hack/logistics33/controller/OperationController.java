package com.hack.logistics33.controller;

import com.hack.logistics33.domain.Info;
import com.hack.logistics33.domain.Product;
import com.hack.logistics33.mapper.OperationMapper;
import com.hack.logistics33.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@CrossOrigin
public class OperationController {
    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private UserMapper userMapper;
    @Autowired
    private OperationMapper operationMapper;

    @GetMapping("/list")
    public LinkedHashMap<String, Object> getList(HttpServletRequest request , @RequestParam("page") int page,
                                                 @RequestParam("page_num") int pageNum,
                                                 @RequestParam( "coloum_name") String coloumName,
                                                 @RequestParam("sort") int sort) throws Exception {

        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<String, Object>();
        HashMap<String, Object> data = new HashMap<String, Object>();

        boolean judge = userMapper.checkToken(request.getHeader("token"));
        if (!judge) {
            hashMap.put("code", 401);
            hashMap.put("message", "token认证失败");
            return hashMap;
        }

        List<Product> list = null;


        if ( !coloumName.equals("count") && !coloumName.equals("price") ) {
            if (sort == 1) {
                list = operationMapper.selectAllProductAsc(coloumName);
            }

            if (sort == 2) {
                list = operationMapper.selectAllProductDesc(coloumName);
            }

            if (sort == 3) {
                list = operationMapper.selectAllProduct();
            }
        }

        if (coloumName.equals("count") || coloumName.equals("price")){
            if (sort == 1) {
                list = operationMapper.selectNumberAllProductAsc(coloumName);
            }

            if (sort == 2) {
                list = operationMapper.selectNumberAllProductDesc(coloumName);
            }

            if (sort == 3) {
                list = operationMapper.selectAllProduct();
            }
        }

        List<Product> lists = new ArrayList<Product>();


        if (((page - 1) * pageNum) > list.size()) {
            hashMap.put("code", 403);
            hashMap.put("message", "分页失败，要求分页数过多");
            return hashMap;
        }

        for (int i = pageNum * (page - 1); i < (((list.size() - (pageNum * (page - 1))) > pageNum ? pageNum : (list.size() - (pageNum * (page - 1)))) + pageNum * (page - 1)); i++) {
            Product product = list.get(i);
            lists.add(product);
        }

        data.put("list", lists);
        data.put("total", list.size());
        hashMap.put("code", 200);
        hashMap.put("message", "获取成功");
        hashMap.put("data", data);

        return hashMap;
    }

    @PostMapping("/list/modify") //如果存在名字相同的，则加上
    public LinkedHashMap<String, Object> changeProduct(HttpServletRequest request, @RequestBody Map<String, Object> resMap) throws Exception {

        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<String, Object>();
        HashMap<String, Object> data = new HashMap<String, Object>();

        boolean judge = userMapper.checkToken(request.getHeader("token"));
        if (!judge) {
            hashMap.put("code", 401);
            hashMap.put("message", "token认证失败");
            return hashMap;
        }


        int id = (int) resMap.get("id");
        String type = resMap.get("type").toString();
        String price =  resMap.get("price").toString();
        double a = Double.parseDouble(price);
        int count = (int) resMap.get("count");
        String name = resMap.get("name").toString();
        String trademark = resMap.get("trademark").toString();

        if (!operationMapper.checkExistProduct(id)) {
            hashMap.put("code", 404);
            hashMap.put("message", "无此id货物");
            return hashMap;
        }

        boolean judge1 = operationMapper.checkExistMany(type , name , trademark , a);

        if (judge1){
            operationMapper.updateExistP(count , type , name , trademark , a);
            operationMapper.deleteProduct(id);
        }

        if (!judge1) {
            operationMapper.changeProduct(type, a, count, name, trademark, id);
        }

        String record = "修改了" + name;
        String operator = userMapper.selectIdByToken(request.getHeader("token"));
        operationMapper.insertChangeInfo(record, operator);

        hashMap.put("code", 200);
        hashMap.put("message", "修改成功");

        return hashMap;
    }

    @PostMapping("/list/delete")
    public LinkedHashMap<String, Object> deleteProduct(HttpServletRequest request, @RequestBody Map<String, Object> resmap) throws Exception {
        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<String, Object>();
        HashMap<String, Object> data = new HashMap<String, Object>();

        boolean judge = userMapper.checkToken(request.getHeader("token"));
        if (!judge) {
            hashMap.put("code", 401);
            hashMap.put("message", "token认证失败");
            return hashMap;
        }

        int id = (int) resmap.get("id");


        if (!operationMapper.checkExistProduct(id)) {
            hashMap.put("code", 404);
            hashMap.put("message", "无此id货物");
            return hashMap;
        }

        operationMapper.deleteProduct(id);
        String name = operationMapper.selectNameById(id);
        String operator = userMapper.selectIdByToken(request.getHeader("token"));
        String record = "删除货物:" + name;
        operationMapper.insertChangeInfo(record, operator);

        hashMap.put("code", 200);
        hashMap.put("message", "修改成功");

        return hashMap;
    }

    @PostMapping("/list/delete/multi")
    public LinkedHashMap<String, Object> deleteProducts(HttpServletRequest request, @RequestBody Map<String, Object> resMap) throws Exception {
        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<String, Object>();
        HashMap<String, Object> data = new HashMap<String, Object>();

        boolean judge = userMapper.checkToken(request.getHeader("token"));
        if (!judge) {
            hashMap.put("code", 401);
            hashMap.put("message", "token认证失败");
            return hashMap;
        }


        ArrayList<Integer> ids = (ArrayList<Integer>) resMap.get("ids");
//        int[] ids = (int[]) resMap.get("ids");

        for (int i = 0; i < ids.size(); i++) {
            if (!operationMapper.checkExistProduct(ids.get(i))) {
                hashMap.put("code", 404);
                hashMap.put("message", "无此id货物");
                return hashMap;
            }

            operationMapper.deleteProduct(ids.get(i));
            String name = operationMapper.selectNameById(ids.get(i));
            String operator = userMapper.selectIdByToken(request.getHeader("token"));
            String record = "删除货物:" + name;
            operationMapper.insertChangeInfo(record, operator);

        }

        hashMap.put("code", 200);
        hashMap.put("message", "修改成功");

        return hashMap;
    }

    @PostMapping("/list/out")  //货物为0的时候删除这一行  //待测试
    public LinkedHashMap<String , Object> out(HttpServletRequest request , @RequestBody Map<String , Object> resMap) throws Exception {
        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<String, Object>();
        HashMap<String, Object> data = new HashMap<String, Object>();

        boolean judge = userMapper.checkToken(request.getHeader("token"));
        if (!judge) {
            hashMap.put("code", 401);
            hashMap.put("message", "token认证失败");
            return hashMap;
        }

        int id = (int) resMap.get("id");
        int count = (int) resMap.get("count");

        if (!operationMapper.checkExistProduct(id)) {
            hashMap.put("code", 404);
            hashMap.put("message", "无此id货物");
            return hashMap;
        }

        int countNow = operationMapper.selectPCount(id);

        if (countNow < count){
            hashMap.put("code" , 400);
            hashMap.put("message" , "选择货物数量过多");
            return hashMap;
        }

        operationMapper.updateP((countNow - count) , id);

        if(operationMapper.selectPCount(id) == 0)
            operationMapper.deleteProduct(id);

        String name = operationMapper.selectNameById(id);
        String operator = userMapper.selectIdByToken(request.getHeader("token"));
        String record = "出货:" + name + count + "件";
        operationMapper.insertChangeInfo(record, operator);

        hashMap.put("code", 200);
        hashMap.put("message", "修改成功");

        return hashMap;
    }

    @PostMapping("/list/out/multi")
    public LinkedHashMap<String , Object> outs(HttpServletRequest request , @RequestBody Map<String , Object> resMap) throws Exception {

        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<String, Object>();
        HashMap<String, Object> data = new HashMap<String, Object>();

        boolean judge = userMapper.checkToken(request.getHeader("token"));
        if (!judge) {
            hashMap.put("code", 401);
            hashMap.put("message", "token认证失败");
            return hashMap;
        }

        ArrayList<Integer> ids = (ArrayList<Integer>) resMap.get("ids");
        int count = (int) resMap.get("count");

        for (int i = 0 ; i < ids.size() ; i++){
            if (!operationMapper.checkExistProduct(ids.get(i))) {
                hashMap.put("code", 404);
                hashMap.put("message", "无此" + ids.get(i) + "货物");
                return hashMap;
            }
        }

        for (int i = 0 ; i < ids.size() ; i++){
            int countNow = operationMapper.selectPCount(ids.get(i));
            if (countNow < count){
                hashMap.put("code" , 400);
                hashMap.put("message" , "选择" + ids.get(i) +"货物数量过多");
                return hashMap;
            }
        }

        for (int i = 0 ; i < ids.size() ; i++){
            int countNow = operationMapper.selectPCount(ids.get(i));
            operationMapper.updateP((countNow - count) , ids.get(i));

            String name = operationMapper.selectNameById(ids.get(i));
            String operator = userMapper.selectIdByToken(request.getHeader("token"));
            String record = "出货:" + name + count + "件";
            operationMapper.insertChangeInfo(record, operator);
        }

        hashMap.put("code", 200);
        hashMap.put("message", "修改成功");
        return hashMap;
    }

    @PostMapping("/list/in")
    public LinkedHashMap<String , Object> inProduct(HttpServletRequest request , @RequestBody Map<String , Object>resMap) throws Exception {
        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<String, Object>();
        HashMap<String, Object> data = new HashMap<String, Object>();

        boolean judge = userMapper.checkToken(request.getHeader("token"));
        if (!judge) {
            hashMap.put("code", 401);
            hashMap.put("message", "token认证失败");
            return hashMap;
        }

        String a = (String) resMap.get("price");
        double aa = Double.parseDouble(a);

        if (operationMapper.checkExistMany(resMap.get("type").toString() ,resMap.get("name").toString() , resMap.get("trademark").toString() , aa)){
            operationMapper.updateExistP((int) resMap.get("count") ,resMap.get("type").toString() ,resMap.get("name").toString() , resMap.get("trademark").toString() ,aa);
        }

        if (!operationMapper.checkExistMany(resMap.get("type").toString() ,resMap.get("name").toString() , resMap.get("trademark").toString() , aa)) {
            operationMapper.insertProduct(resMap.get("type").toString(), resMap.get("name").toString(), resMap.get("trademark").toString()
                    , aa, (int) resMap.get("count"));
        }

        String operator = userMapper.selectIdByToken(request.getHeader("token"));
        String record = "进货:" + resMap.get("name").toString() + (int) resMap.get("count") + "件";
        operationMapper.insertChangeInfo(record, operator);

        hashMap.put("code", 200);
        hashMap.put("message", "进货成功");
        return hashMap;
    }

    @GetMapping("/search")
    public LinkedHashMap<String , Object> search(HttpServletRequest request , @RequestParam("sort") String sort , @RequestParam("content") String content ,
                                                 @RequestParam("page") int page , @RequestParam("page_num") int pageNum) throws Exception {
        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<String, Object>();
        HashMap<String, Object> data = new HashMap<String, Object>();

        boolean judge = userMapper.checkToken(request.getHeader("token"));
        if (!judge) {
            hashMap.put("code", 401);
            hashMap.put("message", "token认证失败");
            return hashMap;
        }

        List<Product> list = null;

        String content1 = "%" + content + "%";

        if (sort.equals("type")) {
            list = operationMapper.selectSpecialPType(content1);
        }

        if (sort.equals("name")) {
            list = operationMapper.selectSpecialPName(content1);
        }

        if (sort.equals("trademark")) {
            list = operationMapper.selectSpecialPTrademark(content1);
        }

        if (sort.equals("price")) {
            list = operationMapper.selectSpecialPPrice(content1);
        }

        if (sort.equals("count")) {
            list = operationMapper.selectSpecialPCount(content);
        }


        List<Product> lists = new ArrayList<Product>();

        if (((page - 1) * pageNum) > list.size()) {
            hashMap.put("code", 403);
            hashMap.put("message", "分页失败，要求分页数过多");
            return hashMap;
        }



        for (int i = pageNum * (page - 1); i < (((list.size() - (pageNum * (page - 1))) > pageNum ? pageNum : (list.size() - (pageNum * (page - 1)))) + pageNum * (page - 1)); i++) {
            Product product = list.get(i);
            lists.add(product);
        }

        data.put("list", lists);
        data.put("total", list.size());
        hashMap.put("code", 200);
        hashMap.put("message", "获取成功");
        hashMap.put("data", data);

        return hashMap;

    }

    @GetMapping("/list/info")
    public LinkedHashMap<String , Object> search(HttpServletRequest request,
                                                 @RequestParam("page") int page , @RequestParam("page_num") int pageNum) throws Exception{
        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<String, Object>();
        HashMap<String, Object> data = new HashMap<String, Object>();

        boolean judge = userMapper.checkToken(request.getHeader("token"));
        if (!judge) {
            hashMap.put("code", 401);
            hashMap.put("message", "token认证失败");
            return hashMap;
        }

        List<Info> list = null;
        list = operationMapper.selectAllInfo();

        List<Info> lists = new ArrayList<Info>();

        if (((page - 1) * pageNum) > list.size()) {
            hashMap.put("code", 403);
            hashMap.put("message", "分页失败，要求分页数过多");
            return hashMap;
        }


        for (int i = pageNum * (page - 1); i < (((list.size() - (pageNum * (page - 1))) > pageNum ? pageNum : (list.size() - (pageNum * (page - 1)))) + pageNum * (page - 1)); i++) {
            Info product = list.get(i);
//            System.out.println("111");
//            System.out.println(product.getOperator());
            lists.add(product);
        }


        data.put("list", lists);
        data.put("total", list.size());
        hashMap.put("code", 200);
        hashMap.put("message", "获取成功");
        hashMap.put("data", data);

        return hashMap;


    }

    @PostMapping("/list/delete/info")
    public LinkedHashMap<String , Object> deleteInfo(HttpServletRequest request ,@RequestBody Map<String , Object> resMap) throws Exception{
        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<String, Object>();
        HashMap<String, Object> data = new HashMap<String, Object>();

        boolean judge = userMapper.checkToken(request.getHeader("token"));
        if (!judge) {
            hashMap.put("code", 401);
            hashMap.put("message", "token认证失败");
            return hashMap;
        }

        int id = (int) resMap.get("id");

        if (!operationMapper.checkExistInfo(id)) {
            hashMap.put("code", 404);
            hashMap.put("message", "无此id信息");
            return hashMap;
        }

        operationMapper.deleteInfo(id);

        hashMap.put("code", 200);
        hashMap.put("message", "修改成功");

        return hashMap;

    }

    @PostMapping("/list/delete/info/multi")
    public LinkedHashMap<String , Object> deleteInfos(HttpServletRequest request ,@RequestBody Map<String , Object> resMap) throws Exception {
        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<String, Object>();
        HashMap<String, Object> data = new HashMap<String, Object>();

        boolean judge = userMapper.checkToken(request.getHeader("token"));
        if (!judge) {
            hashMap.put("code", 401);
            hashMap.put("message", "token认证失败");
            return hashMap;
        }

        ArrayList<Integer> ids = (ArrayList<Integer>) resMap.get("ids");

        for (int i = 0 ; i < ids.size() ; i++){
            if (!operationMapper.checkExistInfo(ids.get(i))){
                hashMap.put("code", 404);
                hashMap.put("message", "无此id信息");
                return hashMap;
            }
        }

        for (int i = 0 ; i < ids.size() ; i++){
            operationMapper.deleteInfo(ids.get(i));
        }

        hashMap.put("code", 200);
        hashMap.put("message", "修改成功");

        return hashMap;
    }
}