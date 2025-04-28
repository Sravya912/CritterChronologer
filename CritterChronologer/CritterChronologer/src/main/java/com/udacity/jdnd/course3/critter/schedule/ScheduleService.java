package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    private ScheduleDTO getDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(schedule.getId());
        scheduleDTO.setDate(schedule.getDate());
        scheduleDTO.setActivities(schedule.getActivities());

        List<Long> employeeIds = schedule.getEmployees().stream()
                .map(Employee::getId)
                .collect(Collectors.toList());
        scheduleDTO.setEmployeeIds(employeeIds);

        List<Long> petIds = schedule.getPets().stream()
                .map(Pet::getId)
                .collect(Collectors.toList());
        scheduleDTO.setPetIds(petIds);

        return scheduleDTO;
    }

    public ScheduleDTO saveSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        schedule.setActivities(scheduleDTO.getActivities());
        schedule.setDate(scheduleDTO.getDate());

        List<Employee> employees = employeeRepository.findAllById(scheduleDTO.getEmployeeIds());
        List<Pet> pets = petRepository.findAllById(scheduleDTO.getPetIds());
        schedule.setPets(pets);
        schedule.setEmployees(employees);

        return getDTO(scheduleRepository.save(schedule));
    }

    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return schedules.stream()
                .map(this::getDTO)
                .collect(Collectors.toList());
    }

    public List<ScheduleDTO> getAllSchedulesForEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        List<Schedule> schedules = scheduleRepository.getAllByEmployeesContains(employee);
        return schedules.stream()
                .map(this::getDTO)
                .collect(Collectors.toList());
    }

    public List<ScheduleDTO> getAllSchedulesForPet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found"));
        List<Schedule> schedules = scheduleRepository.getAllByPetsContains(pet);
        return schedules.stream()
                .map(this::getDTO)
                .collect(Collectors.toList());
    }

    public List<ScheduleDTO> getAllSchedulesForCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        List<Schedule> schedules = scheduleRepository.getAllByPetsIn(customer.getPets());
        return schedules.stream()
                .map(this::getDTO)
                .collect(Collectors.toList());
    }
}
