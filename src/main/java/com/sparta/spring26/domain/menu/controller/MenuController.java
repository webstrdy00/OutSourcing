package com.sparta.spring26.domain.menu.controller;

import com.sparta.spring26.domain.menu.service.MunuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MenuController {
    private final MunuService munuService;
}
