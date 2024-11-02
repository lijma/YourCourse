package com.mf.wetalker.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mf.wetalker.infra.db.entity.Config;
import com.mf.wetalker.infra.db.mapper.ConfigMapper;
import com.mf.wetalker.model.ConfigType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfigService {

    private final ConfigMapper configMapper;


    public JsonNode loadTemplateConfig() {
        Config config = configMapper.selectOne(Wrappers.
                <Config>lambdaQuery().eq(Config::getConfigType, ConfigType.TEMPLATE.name()));
        if (config == null) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(config.getConfigValue());
        } catch (JsonProcessingException e) {
            log.error("failed to load template config, mailformed,", e);
            return null;
        }

    }

    public JsonNode loadToggleConfig() {
        Config toggle = configMapper.selectOne(Wrappers.
                <Config>lambdaQuery().eq(Config::getConfigType, ConfigType.TOGGLE.name()));
        if (toggle == null) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(toggle.getConfigValue());
        } catch (JsonProcessingException e) {
            log.error("failed to load toggle, maleformed format,", e);
            return null;
        }
    }
}


