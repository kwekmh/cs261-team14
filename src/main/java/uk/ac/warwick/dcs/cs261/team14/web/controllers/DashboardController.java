package uk.ac.warwick.dcs.cs261.team14.web.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Ming on 2/11/2017.
 */

@Controller
public class DashboardController {

    @RequestMapping("/")
    public ModelAndView main() {
        ModelAndView mv = new ModelAndView("dashboard/main");

        return mv;
    }
}
