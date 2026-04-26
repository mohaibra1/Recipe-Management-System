package recipes.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipes.dto.RecipeDTO;
import recipes.model.Recipe;
import recipes.repository.RecipeRepository;
import recipes.service.RecipeService;

import java.util.List;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }


    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody RecipeDTO recipeDTO) {
        recipeService.save(recipeDTO);
        return ResponseEntity.ok().build();
    }

//    @GetMapping
//    public ResponseEntity<List<RecipeDTO>> findAll() {
//        return ResponseEntity.ok().body(recipeService.findAll());
//    }
    @GetMapping
    public RecipeDTO findById() {
        List<RecipeDTO> recipeDTO = recipeService.findAllDesc();
        return recipeDTO.getFirst();
    }
}
