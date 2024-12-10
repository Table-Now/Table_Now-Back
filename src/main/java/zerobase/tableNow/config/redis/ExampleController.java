//package zerobase.tableNow.config.redis;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import zerobase.tableNow.config.redis.service.ExampleService;
//
//@RestController
//@RequestMapping("/example")
//@RequiredArgsConstructor
//public class ExampleController {
//    private final ExampleService exampleService;
//
//    @PostMapping("/update")
//    public String updateExample(@RequestParam(name = "key") String key,
//                                @RequestParam(name = "value") Integer value) {
//        exampleService.updateDatabase(key, value);
//        return "Updated successfully";
//    }
//
//    @GetMapping("/get")
//    public Integer getExample(@RequestParam(name = "key") String key) {
//        return exampleService.getValue(key);
//    }
//}
