# jw
jw，全称java web， 模仿spring框架， 实现一个简单可用的java web框架。

本人在工作中学习和使用Spring 3.x框架四年有余，一直专心于使用Spring框架，未求其中机理。前段时间，折腾了php语言，发现了一个个人写的FastPHP框架(见:https://github.com/yeszao/FastPHP) ，小巧玲珑，实现了MVC的全过程，甚觉惊讶！寻思自己也造一个Java的轮子。

翻找市面上现有的Java Web框架，与php框架的百家争鸣的场景不同，java世界的框架有大一统的感觉，Spring之外框架寥寥。首先找到的是JFinal框架(见:http://www.jfinal.com/) ，自觉写的很好，达到了快速开发之目的，只是不喜欢里面的Domain和Dao逻辑混在一起。后来又找到一个学生写的Dump框架(见:https://github.com/yuanguangxin/Dump) ， 里面的变量命名很学生style，但是确实有了FastPHP的简洁明了的feel。看完这几个轮子，我决定站在巨人的肩膀上，照搬Spring各种术语，加上自己感悟，中(Jay的Java所学)西(West的Web框架Spring)合璧，造出jw。

动手做难免有拿来主义之嫌，很多Spring的类名和Annotation名字直接拿来，如DispatcherServlet，@Controller, @Service,@Repository,@RequestMapping,@Autowired,@Transaction,@Before,@Around,@After，@ModelAttribute等等。经过3周的努力，提供了一个web框架所需的基本功能：MVC，IOC，Dao，AOP，事务，JPA，文件上传等。jw中的annotation的使用方法可直接参照Spring中的同名类，只有些许差异。还有很多功能待完善，例如@Valid数据验证支持，等等！

喜欢本框架的同学，欢迎一起造轮子。造轮子不是为了取代Spring，而是为了磨砺Java Web技术，并给自己带来成就感！

熟悉的配方，不一样的味道! jw, 你值得一试！

现着手写一个教程，可移步这里查看http://blog.csdn.net/beautiful5200/

Jay Zhang

写于2016年中秋节 上海寓所

ps: 国庆节期间增加了Validation和Cache支持.
