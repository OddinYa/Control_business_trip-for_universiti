package serjir.universiti.cours_project.business_trips.controllers;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import serjir.universiti.cours_project.business_trips.DAO.TripDAOImpl;
import serjir.universiti.cours_project.business_trips.entity.Employee;
import serjir.universiti.cours_project.business_trips.DAO.EmployeeDAOImpl;
import serjir.universiti.cours_project.business_trips.entity.Trip;


import java.util.List;


@Controller
@RequestMapping("/employee")
public class ControllerEmployee {

    private final EmployeeDAOImpl employeeDAO;

    private final TripDAOImpl tripDAO;


    public ControllerEmployee(EmployeeDAOImpl employeeDAO, TripDAOImpl tripDAO) {
        this.employeeDAO = employeeDAO;
        this.tripDAO = tripDAO;
    }

    @GetMapping
    private String getEmployee(Model model) {
        model.addAttribute("employees", employeeDAO.getEmployees());
        return "employee/employees";
    }

    @GetMapping("/new")
    public String newEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employee/new";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@RequestParam(name = "name") String name,
                         @RequestParam("surname") String surname,
                         @RequestParam("position") String position, Model model) {


        Employee employee = new Employee(name, surname, position);

        employeeDAO.createEntity(employee);

        return "redirect:/employee";
    }

    @GetMapping("/{id}")
    public String getEmployee(@PathVariable("id") int id, Model model) {

        Employee employee = employeeDAO.findTheEntity(id);
        model.addAttribute("employee", employee);

        return "employee/employee";
    }

    @PostMapping(value = "/{id}/delete")
    public String delete(@PathVariable("id") int id, Model model) {
        employeeDAO.deleteEntity(id);
        return "employee/messageDelete";
    }

    @GetMapping("/{id}/edit")
    public String getEdit(@PathVariable("id") int id, Model model) {
        Employee employee = employeeDAO.findTheEntity(id);
        model.addAttribute("employee", employee);
        return "employee/edit";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    public String edit(@RequestParam("id") int id,
                       @RequestParam("name") String name,
                       @RequestParam("surname") String surname,
                       @RequestParam("position") String position,
                       @RequestParam("trip")Trip trip) {


        Employee employee = new Employee();

        employee.setName(name);
        employee.setSurname(surname);
        employee.setPosition(position);
        employee.setTrip(trip);

        employeeDAO.updateEntity(id, employee);

        return "redirect:/employee/" + id;
    }

    @GetMapping("/find")
    public String getFind() {
        return "employee/find";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@RequestParam(name = "id", required = false) Integer id,
                         @RequestParam(name = "name", required = false) String name,
                         @RequestParam(name = "surname", required = false) String surname,
                         Model model) {

        int nonNullParamCount = 0;
        if (id != null) nonNullParamCount++;
        if (name != null) nonNullParamCount++;
        if (surname != null) nonNullParamCount++;

        if (nonNullParamCount == 0) {
            return "error-page";
        }

        List<Employee> listSearch = employeeDAO.searchEntity(id, name, surname);

        model.addAttribute("listSearch", listSearch);

        return "employee/result";

    }

    @GetMapping("/{id}/addtrip")
    public String addTripToEmployee(@PathVariable("id") Integer id, Model model) {

        List<Trip> trips = tripDAO.getTrips();

        model.addAttribute("employee", employeeDAO.findTheEntity(id));
        model.addAttribute("tripList", trips);

        return "trip/listForEmployee";
    }

    @RequestMapping(value = "{id}/addTrip", method = RequestMethod.POST)
    public String addTrip(@PathVariable("id") int id, @RequestParam("tripId") int tripId) {
       Employee employee = employeeDAO.findTheEntity(id);
       Trip trip = tripDAO.findTheEntity(tripId);
       employeeDAO.addTrip(employee,trip);

       return "employee/tripForEmployee";
    }
    @PostMapping(value = "{id}/deletetrip")
    public String deleteTrip(@PathVariable("id") int id){
        Employee employee = employeeDAO.findTheEntity(id);
        Trip trip = employee.getTrip();
        int tripId = trip.getId();
        trip.deleteEmployee(employee);

        tripDAO.updateEntity(tripId,trip);

        employee.setTrip(null);

        employeeDAO.updateEntity(id,employee);

        return "redirect:/employee/" + id;
    }

}





