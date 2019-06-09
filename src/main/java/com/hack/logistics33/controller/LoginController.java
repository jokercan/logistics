package com.hack.logistics33.controller;

import com.hack.logistics33.mapper.LoginMapper;
import com.hack.logistics33.utils.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin
public class LoginController {

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private LoginMapper loginMapper;

    @GetMapping("/a")
    public String test(){
        return "Hello";
    }

    @PostMapping("/login")
    public LinkedHashMap<String , Object> login(@RequestBody Map<String , Object> resMap) throws Exception {
        String id = resMap.get("id").toString();
        String passwd = resMap.get("passwd").toString();

        LinkedHashMap<String , Object> hashMap = new LinkedHashMap<>();
        HashMap<String , Object> data = new HashMap<>();

        boolean userExist = loginMapper.checkExistUser(id);

        if (userExist == false){
            hashMap.put("code" , 403);
            hashMap.put("message" , "登录失败");
            return hashMap;
        }

        String password = loginMapper.selectPasswd(id);
        if (!new Encode().MD5(passwd).equals(password)){
            hashMap.put("code" , 403);
            hashMap.put("message" , "登录失败");
            return hashMap;
        }

        UUID token = UUID.randomUUID();
        loginMapper.updateToken(token.toString() ,id);
        data.put("token" , token);
        hashMap.put("code" , 200);
        hashMap.put("message" , "登录成功");
        hashMap.put("data" , data);
        return hashMap;
    }


}
