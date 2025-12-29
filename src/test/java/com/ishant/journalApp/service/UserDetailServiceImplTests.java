package com.ishant.journalApp.service;


import com.ishant.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ishant.journalApp.entity.User;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Disabled
@ExtendWith(MockitoExtension.class)
public class UserDetailServiceImplTests {

    @InjectMocks
    private UserDetailServiceImpl userDetailService;

    @Mock
    private UserRepository userRepository;

    @Test
    void loadUserByUsernameTest() {

        when(userRepository.findByUserName(anyString()))
                .thenReturn(
                        User.builder()
                                .userName("ram")
                                .password("cnikvr")
                                .roles(List.of("USER"))   // ✅ CORRECT
                                .build()
                );

        // WHEN
        var userDetails = userDetailService.loadUserByUsername("ram");

        // THEN
        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals("ram", userDetails.getUsername());
    }
}
