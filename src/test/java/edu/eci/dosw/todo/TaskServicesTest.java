package edu.eci.dosw.todo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

//import edu.eci.dosw.todo.dto.TaskCreateRequest;
import edu.eci.dosw.todo.dto.TaskResponse;
import edu.eci.dosw.todo.dto.TaskUpdateRequest;
import edu.eci.dosw.todo.entity.TaskEntity;
import edu.eci.dosw.todo.entity.TaskPriority;
import edu.eci.dosw.todo.entity.TaskStatus;
import edu.eci.dosw.todo.repository.TaskRepository;
import edu.eci.dosw.todo.service.TaskServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TaskServicesTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private TaskEntity existingTask;

    @BeforeEach
    public void setUp(){
        existingTask = new TaskEntity();
        existingTask.setId(1L);
        existingTask.setTitle("Estudiar para el parcial");
        existingTask.setDescription("Repasar JPA");
        existingTask.setStatus(TaskStatus.PENDING);
        existingTask.setPriority(TaskPriority.HIGH);
        existingTask.setDueDate(LocalDate.of(2026, 10, 1));
        existingTask.setCreatedAt(LocalDateTime.now());
    }

    /**
     * Este metodo se encarga de buscar todas las tareas creadas y mostrarlas. Para esto, hay que tener en cuenta que
     * ya se tiene una tarea creada anteriormente que es existingTask, donde la salida esperada para esta prueba es
     * la prueba que acabamos de crear.
     * 
     * Ayuda de IA Gen: Al tener un repositorio como provicional, el metodo findAll no va a responder de la manera esperada
     * debido a que siempre va a retornar 0. Para poder solucionar esto se utiliza when, el cual indica al mock que se debe
     * retornar para evitar esta clase de errores. Para que el mock retorne los valores correspondientes, para listas de
     * valores se usa List.of, para un elemento en particular se utuliza Optional.of, pero en caso de que retorne algo vacio
     * se debe utilizar Optional.empty.
     * 
     */
    @Test
    public void findAll_shouldReturnTasks(){
        when(taskRepository.findAll()).thenReturn(List.of(existingTask));
        
        List<TaskResponse> result = taskService.findAll();
        assertEquals(1, result.size());
    }

    /**
     * Este metodo se encarga de ejecutar el metodo findById, en este caso buscamos un Id que sabemos que existe y comprobamos
     * que este se encuentre en la base de datos
     * 
     */
    @Test
    public void findById_shouldReturnTaskWhenExists(){
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        TaskResponse task = taskService.findById(1L);
        assertEquals(1L, task.getId());
    }

    /**
     * Este motodo debe verificar que al momento de tratar de consultar una tarea con un ID el cual no existe lance la excepcion
     * esperada por el sistema, en este caso la excepcion esperada es RuntimeException
     * 
     */
    @Test
    public void findById_shouldThrowExceptionWhenTaskDoesNotExist(){
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.findById(99L));
    }

    /**
     * Este metodo se encarga de agregar una nueva tarea a la base de datos, se debe verificar que la tarea creada fue creada de
     * forma correcta.
     * 
     * Para esta parte, recordemos que no se esta interactuando directamente con el repositorio, por ende, al utilizar create va
     * a retornar un valor null, para esto, tenemos que primero crear la tarea de forma manual e indicarle al mock que debe retornar
     * esa tarea que se creo, para esto, tenemos que entender que el error que ocurre es cuando se llama a save, para esto decimos
     * que cuanddo llegue a ese punto entonces retorne nuestra entidad creada, de esta forma no interactua con el mock creado.
     * 
     */
    // @Test
    // public void create_shouldCreateTask(){
    //     TaskCreateRequest request = new TaskCreateRequest();
    //     request.setTitle("Nueva tarea");
    //     request.setDescription("Descripción de prueba");
    //     request.setPriority(TaskPriority.HIGH);
    //     request.setDueDate(LocalDate.of(2026, 11, 1));

    //     TaskEntity savedEntity = new TaskEntity();
    //     savedEntity.setId(10L);
    //     savedEntity.setTitle("Nueva tarea");
    //     savedEntity.setDescription("Descripción de prueba");
    //     savedEntity.setPriority(TaskPriority.HIGH);
    //     savedEntity.setDueDate(LocalDate.of(2026, 11, 1));
    //     savedEntity.setStatus(TaskStatus.PENDING);
    //     savedEntity.setCreatedAt(LocalDateTime.now());

    //     when(taskRepository.save(any(TaskEntity.class))).thenReturn(savedEntity);

    //     TaskResponse result = taskService.create(request);

    //     assertEquals(10L, result.getId());
    //     assertEquals("Nueva tarea", result.getTitle());
    // }

    /**
     * Este metodo se encarga de realizar la creacion de una tarea, pero, tiene en cuenta el valor predeterminado de los
     * status cuando estos mismos no se mencionan, por ende, este es el valor que tenemos que comparar, donde el status
     * esperado como salida sea "PENDING"
     * 
     */
    // @Test
    // public void create_shouldAssignDefaultStatus(){
    //     TaskCreateRequest request = new TaskCreateRequest();
    //     request.setTitle("Nueva tarea");
    //     request.setDescription("Descripción de prueba");
    //     request.setDueDate(LocalDate.of(2026, 11, 1));

    //     when(taskRepository.save(any(TaskEntity.class))).thenAnswer(invocation -> {
    //         TaskEntity entity = invocation.getArgument(0);
    //         entity.setId(20L);
    //         return entity;
    //     });

    //     TaskResponse result = taskService.create(request);

    //     assertEquals(TaskStatus.PENDING, result.getStatus());

    //     //Se verifica que los otros casos default se configuraron de forma adecuada

    //     assertEquals(TaskPriority.MEDIUM, result.getPriority());
    //     assertEquals(LocalDateTime.now(), result.getCreatedAt());
    // }

    /**
     * Esta prueba se encarga de verificar que una tarea puede ser actualizada de forma adecuada dentr del sistema, para esto
     * se va a tomar una de las tareas ya existentes y se va a editar uno de sus valores
     * 
     */
    @Test
    public void update_shouldUpdateExistingTask(){
        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setTitle("Título actualizado");
        request.setDescription("Nueva descripcion");
        request.setStatus(TaskStatus.IN_PROGRESS);
        request.setPriority(TaskPriority.LOW);
        request.setDueDate(LocalDate.of(2026, 12, 1));

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(TaskEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponse result = taskService.update(1L, request);

        assertEquals("Nueva descripcion", result.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, result.getStatus());
    }

    /**
     * Esta prueba se encarga de detectar cuando una prueba que se esta intentando actualizar no existe dentro de la base
     * de datos, retornando el valor esperado de una excepcion.
     * 
     */
    @Test
    public void update_shouldThrowExceptionWhenTaskDoesNotExist(){
        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setTitle("Título actualizado");
        request.setDescription("Nueva descripcion");
        request.setStatus(TaskStatus.IN_PROGRESS);
        request.setPriority(TaskPriority.LOW);
        request.setDueDate(LocalDate.of(2026, 12, 1));

        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.update(99L, request));
    }

    /**
     * Esta prueba se encarga de eliminar una prueba existente dentro de la base de datos y eliminarla por completo
     * dado como valor esperado
     * 
     */
    @Test
    public void delete_shouldDeleteExistingTask(){
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        taskService.delete(1L);

        verify(taskRepository, times(1)).delete(existingTask);
    }

    /**
     * Este metodo se encarga de verificar que al momento de tratar de eliminar una tarea con un ID que no existe, esta
     * retorne una excepcion que marque el error presentado
     * 
     */
    @Test
    public void delete_shouldThrowExceptionWhenTaskDoesNotExist(){
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.delete(99L));
    }
}
