package net.integr.solvix.config.db

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper
import org.springframework.data.mongodb.core.convert.MappingMongoConverter

@Configuration
class MongoConfig : InitializingBean {

    @Autowired
    val mappingMongoConverter: MappingMongoConverter? = null

    override fun afterPropertiesSet() {
        mappingMongoConverter!!.setTypeMapper(DefaultMongoTypeMapper(null));
    }
}