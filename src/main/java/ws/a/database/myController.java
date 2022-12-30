/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ws.a.database;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ws.a.database.exceptions.NonexistentEntityException;

/**
 *
 * @author MADD
 */
@Controller
@ResponseBody
public class myController
{
        Students data = new Students();
    StudentsJpaController jpactrl = new StudentsJpaController();
    
    @RequestMapping("/getName/{id}")
    public String getName(@PathVariable("id") int id)
    {
        try
        {
            data = jpactrl.findStudents(id);
            return data.getName()+"<br>"+ data.getBirthdate();
        }
        catch (Exception error)
        {
            return "Data tidak ditemukan.";
        }
    }
    
    @RequestMapping("/delete/{id}")
    public String deleteData(@PathVariable("id") int id)
    {
        try
        {
            jpactrl.destroy(id);
            return id + " Deleted.";
        }
        catch (NonexistentEntityException error)
        {
            return id + " Data tidak ditemukan.";
        }
    }
}
