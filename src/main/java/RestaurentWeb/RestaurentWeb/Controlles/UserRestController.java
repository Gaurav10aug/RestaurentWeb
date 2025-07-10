package RestaurentWeb.RestaurentWeb.Controlles;

import RestaurentWeb.RestaurentWeb.Database.DBLoader;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UserRestController {
  @PostMapping("/UserSignUp")
public String registerUser(
        @RequestParam String usname,
        @RequestParam String usemail,
        @RequestParam String uspass,
        @RequestParam MultipartFile usphoto,
        @RequestParam String usaddress,
        @RequestParam String uscontact
) {
    try {
        ResultSet rs = DBLoader.executeQuery("SELECT * FROM user WHERE uemail='" + usemail + "'");

        if (rs.next()) {
            return "Email already registered.";
        }

        // Save photo to disk
        String projectPath = System.getProperty("user.dir");
        String internalPath = "/src/main/resources/static/myUploads";
        String folderName = "/UsermyUploads";
        String orgName = "/" + usphoto.getOriginalFilename();
        String folderPath = projectPath + internalPath + folderName;

        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String fullPath = folderPath + orgName;
        byte[] photoBytes = usphoto.getBytes();
        try (FileOutputStream fos = new FileOutputStream(fullPath)) {
            fos.write(photoBytes);
        }

        // Now re-fetch a fresh insertable ResultSet
        ResultSet rsInsert = DBLoader.executeQuery("SELECT * FROM user WHERE 1=0"); // Empty result set
        rsInsert.moveToInsertRow();
        rsInsert.updateString("uname", usname);
        rsInsert.updateString("uemail", usemail);
        rsInsert.updateString("upassword", uspass);
        rsInsert.updateString("uimage", orgName);
        rsInsert.updateString("uaddress", usaddress);
        rsInsert.updateString("uphone", uscontact);
        rsInsert.insertRow();

        return "User Registered Successfully!";

    } catch (Exception ex) {
        ex.printStackTrace();
        return "Error: " + ex.getMessage();
    }
    }

    @PostMapping("/Userlogin")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session) {
        try {
            ResultSet rs = DBLoader.executeQuery("select * from user where uemail='" + email + "' and upassword='" + password + "'");
            if (rs.next()) 
            {
                session.setAttribute("uemail", email);
                int id = rs.getInt("u_id");
                session.setAttribute("uid", id);
                return "success";
            } else 
            {
                return "failed";
            }
        } catch (Exception ex) 
        {
            ex.printStackTrace();
            return "exception";
        }
    }
    
}
