package cn.iwangwei.utils;

import cn.iwangwei.domain.User;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResourcesUtil {

    private static final ResourcePatternResolver resourceResolver =
            new PathMatchingResourcePatternResolver(User.class.getClassLoader());

    /**
     * 返回路径下所有class
     *
     * @param rootPath 根路径
     * @param pattern  匹配模式
     * @return
     * @throws IOException
     */
    public static List<String> list(String rootPath, String pattern) throws IOException {
        Resource[] resources = resourceResolver.getResources("classpath*:" + rootPath + pattern);

        List<String> resourcePaths = new ArrayList<>();
        if (ArrayUtils.isEmpty(resources)) {
            return resourcePaths;
        }

        for (Resource resource : resources) {
            resourcePaths.add(getClassName(resource.getURL().getPath(), rootPath));
        }

        return resourcePaths;
    }

    /**
     * 获取实现了指定接口的class
     *
     * @param rootPath
     * @param pattern
     * @param clazz
     * @return
     * @throws IOException
     */
    public static List<Class> listClassWithInterface(String rootPath, String pattern, Class clazz) throws IOException {
        List<Class> result = new ArrayList<>();

        List<String> classNames = list(rootPath, pattern);
        for (String className : classNames) {
            Class thisClazz = null;
            try {
                thisClazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                continue;
            }

            Class[] interfaces = thisClazz.getInterfaces();

            if (ArrayUtils.isEmpty(interfaces)) {
                continue;
            }

            for (Class anInterface : interfaces) {
                if (anInterface == clazz) {
                    result.add(thisClazz);
                    break;
                }
            }
        }

        return result;
    }

    private static String getClassName(String filePath, String packagePrex) {
        int start = filePath.indexOf(packagePrex) + 1;
        int end = filePath.lastIndexOf(".class");

        return filePath.substring(start, end).replace("/", ".");
    }
}
