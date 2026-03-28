package com.github.qe7.hephaestus.core.common;

import com.github.qe7.hephaestus.core.feature.setting.AbstractSetting;

/**
 * A parent node for settings.
 */
public interface SettingParent extends ParentNode<AbstractSetting<?>> {
    <S extends AbstractSetting<?>> S setting(S child);
}

