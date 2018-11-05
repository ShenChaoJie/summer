package org.summerframework.core.spi;

import com.google.inject.Injector;
import com.google.inject.Provider;
import org.summerframework.commons.support.logging.Logger;
import org.summerframework.commons.support.logging.LoggerFactory;
import org.summerframework.core.globals.Globals;


/**
 * @Author CJSHEN
 * @Description TODO
 * @Date 2018/11/5 23:01
 * @Version 1.0
 **/
public class SPIProvider implements Provider<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SPIProvider.class);

    private final SPIMapper spi;
    private boolean echo;

    public SPIProvider(SPIMapper spi){
        this.spi = spi;
    }


    @Override
    public Object get() {
        try {
            Injector injector = Globals.get(Injector.class);
            return injector.getInstance(spi.getInstance());
        } finally {
            if(echo){
                LOGGER.debug("创建SPI实现, 接口定义: {}, 绑定名称: {}, 实现类: {}",spi.getSpiClsName(), spi.getName(), spi.getInstance());
                echo = true;
            }
        }
    }
}
