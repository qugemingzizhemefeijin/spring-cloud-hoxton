### springboot之spring-configuration-metadata.json自定义提示

简介说明配置`spring-configuration-metadata.json`文件后，在开发人员的IDE工具使用个人编写的配置读取很有效的在`application.properties`或`application.yml`文件下完成提示。

```

Appendix B. Configuration Metadata
Spring Boot jars include metadata files that provide details of all supported configuration properties. The files are designed to let IDE developers offer contextual help and “code completion” as users are working with application.properties or application.yml files.
 
The majority of the metadata file is generated automatically at compile time by processing all items annotated with @ConfigurationProperties. However, it is possible to write part of the metadata manually for corner cases or more advanced use cases.
```

配置元数据文件位于jar下面。 `META-INF/spring-configuration-metadata.json`它们使用简单的JSON格式，其中的项目分类在“groups”或“properties”下，其他值提示分类在“hints”下，如下例所示：

```
{"groups": [
    {
        "name": "server",
        "type": "org.springframework.boot.autoconfigure.web.ServerProperties",
        "sourceType": "org.springframework.boot.autoconfigure.web.ServerProperties"
    }
    ...
],"properties": [
    {
        "name": "server.port",
        "type": "java.lang.Integer",
        "sourceType": "org.springframework.boot.autoconfigure.web.ServerProperties"
    }
    ...
],"hints": [
    {
        "name": "spring.jpa.hibernate.ddl-auto",
        "values": [
            {
                "value": "none",
                "description": "Disable DDL handling."
            },
            {
                "value": "validate",
                "description": "Validate the schema, make no changes to the database."
            }
        ]
    }
]}
```
group配置属性：
| 名称 | 类型 | 描述 |
| --- | --- | --- |
| name | 组的全名。 该属性是强制性的。 |
| type | 组的数据类型的类名 |
| description | 显示的组的简短描述 |
| sourceType | 贡献此属性的源的类名称 |
| sourceMethod | 贡献此组的方法的全名（包括括号和参数类型）（例如，@ConfigurationProperties 注释的 @Bean 方法的名称）。 如果源方法未知，则可以省略。 |

properties配置属性：
| 名称 | 类型 | 描述 |
| --- | --- | --- |
| name | String | 属性的全名。名称采用小写的周期分隔形式(例如server.address)。此属性是强制性的。 |
| type | String | 	属性的数据类型的完整签名（例如java.lang.String），但也是完整的泛型类型（例如java.util.Map<java.util.String,acme.MyEnum>）。您可以使用此属性来指导用户可以输入的值的类型。为了保持一致性，通过使用其包装对应项（例如，boolean变为java.lang.Boolean）来指定基元的类型。请注意，此类可能是一个复杂类型，它从Stringas绑定的值转换而来。如果类型未知，则可以省略。 |
| description | String | 可以向用户显示的组的简短描述。如果没有可用的描述，则可以省略。建议描述为简短段落，第一行提供简明摘要。描述中的最后一行应以句点（.）结尾。 |
| sourceType | String | 贡献此属性的源的类名称。例如，如果属性来自带注释的类@ConfigurationProperties，则此属性将包含该类的完全限定名称。如果源类型未知，则可以省略。 |
| defaultValue | Object | 默认值，如果未指定属性，则使用该值。如果属性的类型是数组，则它可以是值数组。如果默认值未知，则可以省略。 |

deprecation每个properties元素的属性中包含的JSON对象可以包含以下属性：
| 名称 | 类型 | 描述 |
| --- | --- | --- |
| level String | 弃用级别，可以是warning（默认）或error。当属性具有warning弃用级别时，它仍应绑定在环境中。但是，当它具有error弃用级别时，该属性不再受管理且不受约束。 |
| reason String | 该属性被弃用的原因的简短描述。如果没有可用的原因，可以省略。建议描述为简短段落，第一行提供简明摘要。描述中的最后一行应以句点（.）结尾。 |
| replacement | String | 替换此不推荐使用的属性的属性的全名。如果此属性没有替换，则可以省略。 |

`SpringBoot`的配置文件，IDE会读取`spring-configuration-metadata.json`来提示，自定义Start中，配置类属性可以通过下面的依赖进行编译生成。

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
```

```
Spring Boot’s configuration file handling is quite flexible, and it is often the case that properties may exist that are not bound to a @ConfigurationProperties bean. You may also need to tune some attributes of an existing key. To support such cases and let you provide custom "hints", the annotation processor automatically merges items from META-INF/additional-spring-configuration-metadata.json into the main metadata file.

If you refer to a property that has been detected automatically, the description, default value, and deprecation information are overridden, if specified. If the manual property declaration is not identified in the current module, it is added as a new property.

The format of the additional-spring-configuration-metadata.json file is exactly the same as the regular spring-configuration-metadata.json. The additional properties file is optional. If you do not have any additional properties, do not add the file.
```

`additional-spring-configuration-metadata.json`将会合并到`spring-configuration-metadata.json`中，并覆盖掉相同的说明。说白了就是我们想覆盖掉其他jar包提供的说明信息，可以使用`additional-spring-configuration-metadata.json`。`additional`的英文就是附加的意思。

