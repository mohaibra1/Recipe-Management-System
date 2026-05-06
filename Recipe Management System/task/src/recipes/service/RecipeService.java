package recipes.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import recipes.dto.RecipeDTO;
import recipes.model.Recipe;
import recipes.repository.RecipeRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }


    public Long save(RecipeDTO dto, String userEmail) {
        Recipe recipe = new Recipe();
        // map dto fields to recipe...
        recipe.setName(dto.getName());
        recipe.setCategory(dto.getCategory());
        recipe.setDate(LocalDateTime.now());
        recipe.setDescription(dto.getDescription());
        recipe.setIngredients(dto.getIngredients());
        recipe.setDirections(dto.getDirections());
        recipe.setAuthorEmail(userEmail);
        return recipeRepository.save(recipe).getId();
    }

    public List<RecipeDTO> findAllDesc() {
        List<RecipeDTO> recipeDTOList = new ArrayList<>();
        List<Recipe> recipes = recipeRepository.findAllByOrderByIdDesc();
        recipeDTOList = convertToDTO(recipes);
        return recipeDTOList;
    }

    public RecipeDTO findById(Long id) {
        return recipeRepository.findById(id)
                .map(recipe -> {
                    RecipeDTO dto = new RecipeDTO();
                    dto.setName(recipe.getName());
                    dto.setCategory(recipe.getCategory());
                    dto.setDate(recipe.getDate());
                    dto.setDescription(recipe.getDescription());
                    dto.setIngredients(recipe.getIngredients());
                    dto.setDirections(recipe.getDirections());
                    return dto;
                })
                .orElse(null); // Return null if not found
    }

    public Recipe findRecipeById(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<RecipeDTO> findAll() {
        return convertToDTO(recipeRepository.findAll());
    }

    public List<RecipeDTO> findByCategory(String category) {
        return convertToDTO(recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(category));
    }

    public List<RecipeDTO> findByName(String name) {
        return convertToDTO(recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(name));
    }

    public void update(Long id, RecipeDTO recipeDTO) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Update fields from DTO
        recipe.setName(recipeDTO.getName());
        recipe.setCategory(recipeDTO.getCategory());
        recipe.setDescription(recipeDTO.getDescription());
        recipe.setIngredients(recipeDTO.getIngredients());
        recipe.setDirections(recipeDTO.getDirections());
        recipe.setDate(LocalDateTime.now()); // Update the date field

        recipeRepository.save(recipe);
    }

    public void deleteById(Long id, String currentUserEmail) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!recipe.getAuthorEmail().equals(currentUserEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        recipeRepository.deleteById(id);
    }

    public List<RecipeDTO> convertToDTO(List<Recipe> recipes) {
        List<RecipeDTO>  recipeDTOs = new ArrayList<>();
        for(Recipe createdRecipe : recipes) {
            RecipeDTO createdRecipeDTO = new RecipeDTO();
            createdRecipeDTO.setName(createdRecipe.getName());
            createdRecipeDTO.setCategory(createdRecipe.getCategory());
            createdRecipeDTO.setDate(createdRecipe.getDate());
            createdRecipeDTO.setDescription(createdRecipe.getDescription());
            createdRecipeDTO.setIngredients(createdRecipe.getIngredients());
            createdRecipeDTO.setDirections(createdRecipe.getDirections());

            recipeDTOs.add(createdRecipeDTO);
        }
        return recipeDTOs;
    }

}
