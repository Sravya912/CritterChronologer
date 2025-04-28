package com.udacity.jdnd.course3.critter.user;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setName(employeeDTO.getName());
        employee.setAvailDay(employeeDTO.getDaysAvailable());
        employee.setSkills(employeeDTO.getSkills());
        employee.setId(employeeDTO.getId());

        return getDTO(employeeRepository.save(employee));
    }

    private EmployeeDTO getDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());
        employeeDTO.setSkills(employee.getSkills());
        employeeDTO.setDaysAvailable(employee.getAvailDay());
        return employeeDTO;
    }

    public EmployeeDTO getEmployeeById(Long employeeId) {
        return getDTO(employeeRepository.getOne(employeeId));
    }

    public void setEmployeeAvailability(Set<DayOfWeek> days, Long employeeId) {
        Employee employee = employeeRepository.getOne(employeeId);
        employee.setAvailDay(days);
        employeeRepository.save(employee);
    }

    public List<EmployeeDTO> getEmployeeForService(LocalDate date, Set<EmployeeSkill> skills) {
        DayOfWeek req = date.getDayOfWeek();
        List<Employee> availableEmployees = employeeRepository.getAllByAvailDay(req);

        List<Employee> matchingEmployees = availableEmployees.stream()
                .filter(e -> e.getSkills().containsAll(skills))
                .collect(Collectors.toList());

        return matchingEmployees.stream()
                .map(this::getDTO)
                .collect(Collectors.toList());
    }
}
