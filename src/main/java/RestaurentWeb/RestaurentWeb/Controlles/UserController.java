package RestaurentWeb.RestaurentWeb.Controlles;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {
    
    @GetMapping("/index")
    public String go(){
        return "index";
    }
    
    @GetMapping("/UserLogin")
    public String go1(){
        return "UserLogin";
    }
    
    @GetMapping("/AdminLogin")
    public String go2(){
        return "AdminLogin";
    }
    
    @GetMapping("/UserSignUp")
    public String go3(){
        return "UserSignUp";
    }
    
    @GetMapping("/Bookings")
    public String go4(){
        return "Bookings";
    }
}
