package uz.pdp.learningcenterapp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.pdp.learningcenterapp.entity.User;
import uz.pdp.learningcenterapp.repo.MyUserRepo;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final MyUserRepo myUserRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User byPhoneNumber = myUserRepo.findByPhoneNumber(username);
        System.out.println(byPhoneNumber);
        return byPhoneNumber;
    }


}
