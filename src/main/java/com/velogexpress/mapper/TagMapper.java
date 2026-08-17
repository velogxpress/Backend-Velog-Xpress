package com.velogexpress.mapper;

import com.velogexpress.entity.Tag;
import com.velogexpress.model.TagModel;

public class TagMapper {
    public static TagModel mapToTagModel(Tag tag) {
        return new TagModel(
                tag.getId(),
                tag.getDescription(),
                tag.getQrcode()
        );
    }
    public static Tag mapToTag(TagModel tagModel) {
        return new Tag(
                tagModel.getId(),
                tagModel.getDescription(),
                tagModel.getQrcode()
        );
    }
}
