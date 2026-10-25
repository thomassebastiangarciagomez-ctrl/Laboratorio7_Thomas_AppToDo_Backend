package edu.eci.dosw.todo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import edu.eci.dosw.todo.controller.TaskController;
//import edu.eci.dosw.todo.dto.TaskCreateRequest;
import edu.eci.dosw.todo.dto.TaskResponse;
import edu.eci.dosw.todo.dto.TaskUpdateRequest;
import edu.eci.dosw.todo.entity.TaskEntity;
import edu.eci.dosw.todo.entity.TaskPriority;
import edu.eci.dosw.todo.entity.TaskStatus;
import edu.eci.dosw.todo.exception.TaskNotFoundException;
import edu.eci.dosw.todo.service.TaskService;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskService taskService;

    private TaskResponse sampleResponse;

    @BeforeEach
    public void setUp(){
        TaskEntity entity = new TaskEntity();
        entity.setId(1L);
        entity.setTitle("Terminar laboratorio DOSW");
        entity.setDescription("Completar pruebas del backend");
        entity.setStatus(TaskStatus.PENDING);
        entity.setPriority(TaskPriority.HIGH);
        entity.setDueDate(LocalDate.of(2026, 9, 25));
        entity.setCreatedAt(LocalDateTime.now());

        sampleResponse = TaskResponse.fromEntity(entity);
    }

    // TEST CON GET

    /**
     * Esta prueba se encarga de realizar una consulta a la base de datos de todas las tareas que estan disponibles
     * en este momento, aqui solamente se espera como codigo resultante 200 (OK)
     * 
     */
    @Test
    public void should_GetOk() throws Exception{
        when(taskService.findAll()).thenReturn(List.of(sampleResponse));

        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk());
    }

    /**
     * Esta prueba se encarga de realizar una consulta valida a una de las tareas creadas en el sistema, de tal forma que
     * el codigo de respuesta esperado es el 200
     * 
     */
    @Test 
    public void should_findByIdOk() throws Exception{
        when(taskService.findById(1L)).thenReturn(sampleResponse);

        mockMvc.perform(get("/api/v1/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    /**
     * Esta prueba se encarga de realizar una consulta invalida a una de las tareas creadas en el sistema, de tal forma que
     * el codigo de respuesta esperado es el 404
     * 
     */
    @Test
    public void should_not_findById() throws Exception{
        when(taskService.findById(99L)).thenThrow(new TaskNotFoundException(99L));

        mockMvc.perform(get("/api/v1/tasks/99"))
                .andExpect(status().isNotFound());
    }


    // TEST CON POST

    /**
     * Esta prueba se encarga de comprobar que se sube de manera correcta la informacion a la base de datos, donde el valor
     * esperado de retorno es 201
     * 
     */
    // @Test 
    // public void should_postTask() throws Exception{
    //     TaskCreateRequest request = new TaskCreateRequest();
    //     request.setTitle("Terminar laboratorio DOSW");
    //     request.setDescription("Completar pruebas del backend");
    //     request.setPriority(TaskPriority.HIGH);
    //     request.setDueDate(LocalDate.of(2026, 9, 25));

    //     when(taskService.create(any(TaskCreateRequest.class))).thenReturn(sampleResponse);

    //     mockMvc.perform(post("/api/v1/tasks").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
    //         .andExpect(status().isCreated())
    //         .andExpect(jsonPath("$.id").value(1))
    //         .andExpect(jsonPath("$.status").value("PENDING"));
    // }

    /**
     * Esta prueba se encarga de realizar una prueba invalida para poder determinar que esten funcionando los casos de error
     * se espera que el codigo resultado sea 400
     * 
     */
    // @Test 
    // public void should_not_postTask() throws Exception{
    //     TaskCreateRequest request = new TaskCreateRequest();
    //     request.setPriority(TaskPriority.HIGH);

    //     when(taskService.create(any(TaskCreateRequest.class))).thenReturn(sampleResponse);

    //     mockMvc.perform(post("/api/v1/tasks").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
    //         .andExpect(status().isBadRequest())
    //         .andExpect(jsonPath("$.status").value(400));
    // }


    // TEST CON PUT

    /**
     * Esta prueba se encarga de analizar si se realizo de forma correcta la actualizacion de los datos de una de las 
     * tareas planteadas, el codigo esperado es 200.
     * 
     */
    @Test 
    public void should_putOk() throws Exception{
        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setTitle("Terminar laboratorio DOSW");
        request.setDescription("Completar pruebas del backend");
        request.setPriority(TaskPriority.HIGH);
        request.setStatus(TaskStatus.IN_PROGRESS);
        request.setDueDate(LocalDate.of(2026, 9, 25));

        when(taskService.update(eq(1L), any(TaskUpdateRequest.class))).thenReturn(sampleResponse);

        mockMvc.perform(put("/api/v1/tasks/1").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    /**
     * Esta prueba se encarga de verificar que no se logra actualizar una tarea la cual no existe dentro de la base de datos
     * para que se actualice la misma, el codigo esperado es 404.
     * 
     */
    @Test 
    public void should_not_put() throws Exception{
        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setTitle("Terminar laboratorio DOSW");
        request.setDescription("Completar pruebas del backend");
        request.setPriority(TaskPriority.HIGH);
        request.setStatus(TaskStatus.IN_PROGRESS);
        request.setDueDate(LocalDate.of(2026, 9, 25));

        when(taskService.update(eq(99L), any(TaskUpdateRequest.class))).thenThrow(new TaskNotFoundException(99L));

        mockMvc.perform(put("/api/v1/tasks/99").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }


    // TEST CON DELETE

    /**
     * Esta prueba se encarga de eliminar alguna tarea de forma efectiva, donde el codigo de respuesta esperado por parte
     * del sistema es 204.
     * 
     */
    @Test 
    public void should_deleteTask() throws Exception{
        mockMvc.perform(delete("/api/v1/tasks/1"))
            .andExpect(status().isNoContent());
    }

}
