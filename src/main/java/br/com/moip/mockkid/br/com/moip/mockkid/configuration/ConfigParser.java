package br.com.moip.mockkid.br.com.moip.mockkid.configuration;


import br.com.moip.mockkid.model.Config;
import br.com.moip.mockkid.model.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigParser {

    private static Map<String, Configuration> configurations = new LinkedHashMap<>();

    public static void main(String[] args) {
    		ConfigParser.openConfigurations();
    }
    
    public static void openConfigurations(){
        File f = new File(ConfigParser.class.getClassLoader().getResource("configuration/teste.yaml").getFile());
        System.out.println(toConfiguration(f).getConfiguration().getEndpoint().getUrl());
    }

    public static Config toConfiguration(File file){
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Config config = null;
        try {
            config = mapper.readValue(file, Config.class);
            System.out.println(ReflectionToStringBuilder.toString(config, ToStringStyle.MULTI_LINE_STYLE));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return config;
    }


}
