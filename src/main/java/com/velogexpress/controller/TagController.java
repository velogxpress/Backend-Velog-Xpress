package com.velogexpress.controller;

import org.springframework.cache.annotation.Cacheable;

import com.velogexpress.model.TagModel;
import com.velogexpress.service.TagService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/tag")
public class TagController {
    private TagService tagService;
    @Cacheable(cacheNames = "tag")
    @GetMapping("/bin/{desc}")
    public ResponseEntity<TagModel> getTag(@PathVariable String desc) {
        TagModel tagModel = tagService.getTag(desc);
        return ResponseEntity.ok(tagModel);
    }
}
