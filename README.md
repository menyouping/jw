# jw
jw，全称java web， 模仿spring框架， 实现一个简单可用的java web框架。

本人在工作中学习和使用Spring 3.x框架四年有余，一直专心于使用Spring框架，未求其中机理。前段时间，折腾了php语言，发现了一个个人写的FastPHP框架(见:https://github.com/yeszao/FastPHP) ，小巧玲珑，实现了MVC的全过程，甚觉惊讶！寻思自己也造一个Java的轮子。

翻找市面上现有的Java Web框架，首先找到的是JFinal框架(见:http://www.jfinal.com/) ，自觉写的很好，只是不喜欢里面的Domain和Dao逻辑混在一起。后来又找到一个大学生写的框架Dump(见:https://github.com/yuanguangxin/Dump) ， 虽然里面的变量命名风格很学生style，但是有点FastPHP的简洁明了的feel。看完这几个轮子，我决定直接根据自己对Spring的使用情况的理解，造出一个轮子，并取名jw，这便是此框架的由来。

动手做难免有拿来主义之嫌，很多Spring的类名和Annotation名字直接拉来，如DispatcherServlet，@Controller, @Service,@Repository,@RequestMapping,@Autowired,@Transaction,@Before,@Around,@After，@ModelAttribute等等。经过3周的努力，提供了一个web框架所需的基本功能：MVC，IOC，Dao，AOP，事务，JPA，文件上传等
很多使用经验可直接根据Spring的用法来推断，只有些许差异。还有很多功能待完善，例如@Valid数据验证支持，等等！

喜欢本框架的同学，欢迎一起造轮子。造轮子不是为了取代Spring，而是为了掌握Java Web技术，并给自己带来成就感！

熟悉的配方，不一样的味道! jw, 你值得一试！

Jay Zhang

写于2016年中秋节 上海寓所
