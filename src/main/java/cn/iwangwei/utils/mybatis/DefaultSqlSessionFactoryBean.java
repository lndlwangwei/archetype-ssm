package cn.iwangwei.utils.mybatis;

import cn.iwangwei.dao.UserDao;
import cn.iwangwei.utils.ResourcesUtil;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * 重写SqlSessionFactoryBean，使得可以批量添加TypeHandler和TypeAlias
 *
 * @author wangwei
 * @since 1.0
 */
public class DefaultSqlSessionFactoryBean extends SqlSessionFactoryBean {

    private Resource configLocation;

    private Properties configurationProperties;

    private Resource[] mapperLocations;

    public static void main(String[] args) {
        System.out.println(UserDao.class.getDeclaredMethods().length);
    }

    @Override
    protected SqlSessionFactory buildSqlSessionFactory() throws IOException {

        XMLConfigBuilder xmlConfigBuilder =
            new XMLConfigBuilder(this.configLocation.getInputStream(), null, this.configurationProperties);
        Configuration configuration = xmlConfigBuilder.getConfiguration();

        Document document = getBaseMapper();
        Element mapper = document.getRootElement();
        Arrays.stream(this.mapperLocations).forEach(resource -> {
            if ("base-mapper.xml".equals(resource.getFilename()))
                return;
            try {
                Document mapperDocument = getDocument(resource.getInputStream());
                String repositoryName = mapperDocument.getRootElement().attribute("namespace").getValue();
                Class repositoryClass = Class.forName(repositoryName);

                System.out.println(repositoryClass.getMethods());
                mapperDocument.getRootElement().getXPathResult(1);
                System.out.println();
            } catch (IOException e) {
                // throw exception
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        this.setConfiguration(configuration);
        // 解析mybatis-config.xml中的配置
        parseXmlConfig(xmlConfigBuilder);

        return super.buildSqlSessionFactory();
    }

    @Override
    public void setConfigLocation(Resource configLocation) {
        this.configLocation = configLocation;
        super.setConfigLocation(configLocation);
    }

    @Override
    public void setConfigurationProperties(Properties configurationProperties) {
        this.configurationProperties = configurationProperties;
        super.setConfigurationProperties(configurationProperties);
    }

    /**
     * 解析mybatis-config.xml中的配置
     *
     * @param xmlConfigBuilder
     * @throws NestedIOException
     */
    private void parseXmlConfig(XMLConfigBuilder xmlConfigBuilder) throws NestedIOException {
        try {
            xmlConfigBuilder.parse();
        } catch (Exception ex) {
            throw new NestedIOException(
                "Failed to parse config resource: " + this.configLocation, ex);
        } finally {
            ErrorContext.instance().reset();
        }
    }

    public Resource[] getMapperLocations() {
        return mapperLocations;
    }

    @Override
    public void setMapperLocations(Resource[] mapperLocations) {
        this.mapperLocations = mapperLocations;
        super.setMapperLocations(null);
    }

    private Document getDocument(InputStream inputStream) {
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(inputStream);
            return document;
        } catch (Exception e) {
            // throw Exception
        }
        return null;
    }

    private Document getBaseMapper() {
        SAXReader reader = new SAXReader();
        Resource baseMapper = Arrays.stream(mapperLocations).filter(resource -> "a_base-mapper.xml".equals(resource.getFilename())).findFirst().get();
        Document document = null;
        try {
            document = reader.read(baseMapper.getInputStream());
            return  document;

//            Element mapper = document.getRootElement();
//            mapper.addAttribute("namespace", "cn.iwangwei.repository.wangweiRepository");
//
//            System.out.println(String.format("document : %s", document.getText()));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
