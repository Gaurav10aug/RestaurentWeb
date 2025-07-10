package RestaurentWeb.RestaurentWeb.Controlles;

import RestaurentWeb.RestaurentWeb.Database.DBLoader;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.OutputStream;
import java.sql.ResultSet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AdminRestController {

    @PostMapping("/Adminlogin")
    public String login(@RequestParam("email") String email,
            @RequestParam("password") String password) {
        try {
            System.out.println("Email: " + email);
            System.out.println("Password: " + password);

            ResultSet rs = DBLoader.executeQuery(
                    "SELECT * FROM admin WHERE ad_email='" + email + "' AND ad_pass='" + password + "'"
            );

            if (rs.next()) {
                return "success";
            } else {
                return "failed";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }
    }

    @PostMapping("/AdminManageChefs")
    public String addChef(@RequestParam String name,
            @RequestParam String phone,
            @RequestParam String address,
            @RequestParam String cuisine,
            @RequestParam String dish,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam MultipartFile photo) {

        try {
            ResultSet rs = DBLoader.executeQuery("select * from chefs where email='" + email + "'");
            if (rs.next()) {
                return "Chef with this email already exists.";
            } else {
                rs.moveToInsertRow();
                rs.updateString("name", name);
                rs.updateString("phone", phone);
                rs.updateString("address", address);
                rs.updateString("cuisine", cuisine);
                rs.updateString("dish", dish);
                rs.updateString("email", email);
                rs.updateString("password", password);
                rs.updateBytes("photo", photo.getBytes()); // ✅ store image
                rs.insertRow();
                return "Chef added successfully";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    @GetMapping("/GetChefPhoto")
    public void getChefPhoto(@RequestParam("chef_id") int chef_id, HttpServletResponse response) {
        try {
            ResultSet rs = DBLoader.executeQuery("select * from chefs where chef_id=" + chef_id);
            if (rs.next()) {
                byte[] img = rs.getBytes("photo");
                response.setContentType("image/jpeg");
                OutputStream os = response.getOutputStream();
                os.write(img);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/AdminGetAllChefs")
    public String getAllChefs() {
        StringBuilder ans = new StringBuilder();
        try {
            ResultSet rs = DBLoader.executeQuery("select * from chefs");

            // Container for all cards
            ans.append("<div style='display: flex; flex-wrap: wrap; justify-content: center; gap: 20px; padding: 20px;'>");

            while (rs.next()) {
                int chef_id = rs.getInt("chef_id");
                String name = rs.getString("name");
                String cuisine = rs.getString("cuisine");
                String dish = rs.getString("dish");
                String email = rs.getString("email");

                // Card container
                ans.append("<div style='width: 300px; background: #1e1e1e; color: white; border-radius: 10px; ")
                        .append("box-shadow: 0 4px 8px rgba(255,255,255,0.1); overflow: hidden;'>");

                // Chef Image
                ans.append("<img src='/GetChefPhoto?chef_id=" + chef_id + "' ")
                        .append("style='width: 100%; height: 200px; object-fit: cover;'>");

                // Card Body
                ans.append("<div style='padding: 15px;'>");

                ans.append("<h5 style='margin-bottom: 10px; color: white;'>").append(name).append("</h5>");
                ans.append("<p style='margin: 4px 0; color: white;'>Cuisine: ").append(cuisine).append("</p>");
                ans.append("<p style='margin: 4px 0; color: white;'>Dish: ").append(dish).append("</p>");
                ans.append("<p style='margin: 4px 0; color: white;'>Email: ").append(email).append("</p>");

                // Delete Button
                ans.append("<button onclick='deleteChef(").append(chef_id).append(")' ")
                        .append("style='margin-top: 10px; background-color: red; color: white; ")
                        .append("border: none; padding: 8px 16px; border-radius: 5px;'>Delete</button>");

                ans.append("</div>"); // close card body
                ans.append("</div>"); // close card
            }

            ans.append("</div>"); // close card container

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ans.toString();
    }

    @GetMapping("/AdminDeleteChef")
    public String deleteChef(@RequestParam int chef_id) {
        try {
            ResultSet rs = DBLoader.executeQuery("select * from chefs where chef_id=" + chef_id);
            if (rs.next()) {
                rs.deleteRow();
                return "success";
            } else {
                return "failure";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }
    }

}
