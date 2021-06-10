package com.bat.security.security;

import com.bat.base.utils.MD5;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultPasswordEncoder implements PasswordEncoder {


    public DefaultPasswordEncoder(){
        this(-1);
    }

    public DefaultPasswordEncoder(int strLength){

    }

    @Override
    public String encode(CharSequence charSequence) {
        return MD5.encrypt(charSequence.toString());
    }

    @Override
    public boolean matches(CharSequence charSequence, String currentPassword) {
        return currentPassword.equals(this.encode(charSequence.toString()));
    }
}
