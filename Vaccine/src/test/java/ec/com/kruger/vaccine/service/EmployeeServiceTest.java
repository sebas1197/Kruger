package ec.com.kruger.vaccine.service;

import ec.com.kruger.vaccine.dao.EmployeeRepository;
import ec.com.kruger.vaccine.dao.UserRespository;
import ec.com.kruger.vaccine.dao.VaccineRepository;
import ec.com.kruger.vaccine.dao.VaccineTypeRepository;
import ec.com.kruger.vaccine.dto.EmployeeRequest;
import ec.com.kruger.vaccine.dto.DataEmployeeRequest;
import ec.com.kruger.vaccine.dto.LoginRequest;
import ec.com.kruger.vaccine.dto.VaccineRequest;
import ec.com.kruger.vaccine.model.Employee;
import ec.com.kruger.vaccine.model.Rol;
import ec.com.kruger.vaccine.model.Vaccine;
import ec.com.kruger.vaccine.model.VaccineType;
import ec.com.kruger.vaccine.services.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private UserRespository userRespository;
    @Mock
    private VaccineTypeRepository vaccineTypeRepository;
    @Mock
    private VaccineRepository vaccinationDetailRepository;

    @InjectMocks
    private EmployeeService employeeService;
    private Employee employee1;
    private Employee employee2;
    private List<Employee> employeeList;
    private EmployeeRequest employeRequest;
    private DataEmployeeRequest dataEmployeeRequest;
    private VaccineType vaccineType;
    private Vaccine vaccine1;
    private Vaccine vaccine2;
    private Vaccine vaccine3;
    private List<Vaccine> vaccineList;

    @BeforeEach
    void setUp() {
        this.employee1 = Employee.builder()
                .id(1)
                .names("Edison Sebastián")
                .lastnames("Landázuri Galarza")
                .identification("1726220542")
                .email("sebas.landa1197@gmail.com")
                .build();
        this.employee2 = Employee.builder()
                .id(2)
                .names("Juan Francisco")
                .lastnames("Calero Hidalgo")
                .identification("1707180392")
                .email("juan123@gmail.com")
                .build();

        this.vaccineType = new VaccineType();
        vaccineType.setId_vaccine(1);
        vaccineType.setName("AstraZeneca");

        this.vaccine1 = new Vaccine();
        vaccine1.setEmployee(employee1);
        vaccine1.setVaccinationDate(new Date());
        vaccine1.setVaccineType(vaccineType);
        vaccine1.setId(1);
        vaccine1.setVaccinationDose(3);

        this.vaccine2 = new Vaccine();
        vaccine2.setEmployee(employee2);
        vaccine2.setVaccinationDate(new Date());
        vaccine2.setVaccineType(vaccineType);
        vaccine2.setId(1);
        vaccine2.setVaccinationDose(1);

        this.vaccine3 = new Vaccine();
        vaccine3.setEmployee(employee2);
        vaccine3.setVaccinationDate(new Date());
        vaccine3.setVaccineType(vaccineType);
        vaccine3.setId(1);
        vaccine3.setVaccinationDose(2);

        this.vaccineList = new ArrayList<>();
        vaccineList.add(vaccine1);
        vaccineList.add(vaccine2);
        vaccineList.add(vaccine3);

        this.dataEmployeeRequest = new DataEmployeeRequest();
        this.employeeList = new ArrayList<>();
        employeeList.add(employee1);
        employeeList.add(employee2);
        this.employeRequest = new EmployeeRequest();
       
    }
    
    @Test
    void getAllEmployees() {
        try {
            when(employeeRepository.findAll()).thenReturn(employeeList);
            Assertions.assertEquals(employeeList,employeeService.getAllEmployees());
        } catch (Exception e){
            log.error("{}", e.getMessage());
        }
    }

    @Test
    void givenIdReturnEmployee() {
        try {
            when(employeeRepository.findById(any())).thenReturn(java.util.Optional.of(employee1));
            Assertions.assertEquals(employee1,employeeService.getEmployeeById(1));
        } catch (Exception e){
            log.error("{}", e.getMessage());
        }
    }
 
    @Test
    void givenIdDeleteEmployee() {
        try {
            when(employeeRepository.findById(any())).thenReturn(java.util.Optional.of(employee1));
            employeeService.deleteEmployee(1);
            verify(employeeRepository, times(1)).delete(employee1);
        } catch (Exception e){
            log.error("{}", e.getMessage());
        }
    }

    @Test
    void givenVaccinationStatusReturnEmployees() {
        try {
            List<Employee> testEmployees = new ArrayList<>();
            testEmployees.add(employee2);
            when(employeeRepository.findByVaccinationStatus(true)).thenReturn(testEmployees);
            Assertions.assertEquals(testEmployees,employeeService.findByVaccinationStatus(true));
        } catch (Exception e){
            log.error("{}", e.getMessage());
        }
    }

    @Test
    void givenVaccineTypeReturnEmployees() {
        try {
            when(vaccineTypeRepository.findByName(any())).thenReturn(java.util.Optional.of(vaccineType));
            when(vaccinationDetailRepository.findByVaccineType(vaccineType)).thenReturn(vaccineList);
            Assertions.assertEquals(employeeList,employeeService.findByVaccineType("AstraZeneca"));
        } catch (Exception e){
            log.error("{}", e.getMessage());
        }
    }

    @Test
    void givenDateRangeVaccinationReturnEmployees() {
        try {
            Date currentDate = new Date();
            when(vaccinationDetailRepository.findByVaccinationDateBetween(new Date(currentDate.getTime() - 86400000),new Date(currentDate.getTime() + 86400000))).thenReturn(vaccineList);
            Assertions.assertEquals(employeeList,employeeService.findByDates(new Date(currentDate.getTime() - 86400000), new Date(currentDate.getTime() + 86400000)));
        } catch (Exception e){
            log.error("{}", e.getMessage());
        }
    }
     
    void givenUpdateEmployeeRQAndIdUpdateEmployee() {
        try {
            when(vaccineTypeRepository.findById(any())).thenReturn(java.util.Optional.of(vaccineType));
            when(employeeRepository.findById(any())).thenReturn(java.util.Optional.of(employee2));
            dataEmployeeRequest.setEmail("juancalero@outlook.com");
            dataEmployeeRequest.setNames("Juan Francisco");
            dataEmployeeRequest.setLastnames("Calero Hidalgo");
            dataEmployeeRequest.setHomeAddress("Quito Norte Carapungo");
            dataEmployeeRequest.setBirthday(new Date());
            dataEmployeeRequest.setCellPhone("0983877482");
            dataEmployeeRequest.setVaccinationStatus(true);
            List<VaccineRequest> vaccinationDetailsRQ = new ArrayList<>();
            VaccineRequest vaccinationDetailRQ = new VaccineRequest ();
            vaccinationDetailRQ.setVaccineType(1);
            vaccinationDetailRQ.setVaccinationDate(new Date());
            vaccinationDetailRQ.setVaccinationDose(1);
            vaccinationDetailsRQ.add(vaccinationDetailRQ);
            dataEmployeeRequest.setVaccinationDetails(vaccinationDetailsRQ);
            employeeService.updateEmployee(2,dataEmployeeRequest, Rol.ADMINISTRADOR.toString());
            verify(employeeRepository, times(1)).save(employee2);
        } catch (Exception e){
            log.error("{}", e.getMessage());
        }
    }

   
    void givenEmployeeRegisterEmployee() {
        try {
            when(employeeRepository.findByIdentification(any())).thenReturn(new ArrayList<>());
            when(employeeRepository.findByEmail(any())).thenReturn(new ArrayList<>());
            when(userRespository.findByUsername(any())).thenReturn(Optional.empty());
            employeRequest.setEmail("sebas.landa1197@gmail.com");
            employeRequest.setIdentification("1726220542");
            employeRequest.setNames("Edison Sebastián");
            employeRequest.setLastnames("Landázuri Galarza");
            LoginRequest generatedCredentials = LoginRequest.builder()
                    .username("EDI172")
                    .password("EDI172622")
                    .build();
            Assertions.assertEquals(generatedCredentials, employeeService.registerEmployee(employeRequest));
        }catch (Exception e){
            log.error("{}", e.getMessage());
        }
    }
}