package com.velogexpress.service.impl;

import com.velogexpress.entity.Tag;
import com.velogexpress.mapper.TagMapper;
import com.velogexpress.model.TagModel;
import com.velogexpress.repository.TagRepository;
import com.velogexpress.service.TagService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TagServiceImpl implements TagService {
    private TagRepository tagRepository;

    @Override
    public TagModel getTag(String tag) {
        Tag tags=tagRepository.findTag(tag);
        if(tags==null) {
            return null;
        }else {
            return TagMapper.mapToTagModel(tags);
        }
    }
}
