package RestaurentWeb.RestaurentWeb.Controlles;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    
    @GetMapping("/AdminLogin")
    public String go1(){
        return "AdminLogin";
    }
    
    @GetMapping("/AdminHome")
    public String go2(){
        return "AdminHome";
    }
    
    @GetMapping("/AdminManageChefs")
    public String go3(){
        return "AdminManageChefs";
    }
    
    @GetMapping("/AdminMaageItems")
    public String go4(){
        return "AdminMaageItems";
    }
}
