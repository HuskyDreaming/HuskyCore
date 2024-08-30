package com.huskydreaming.huskycore.interfaces;

import com.huskydreaming.huskycore.interfaces.Service;
import com.huskydreaming.huskycore.storage.Yaml;

public interface DefaultService extends Service {


    Yaml getMenu();
}