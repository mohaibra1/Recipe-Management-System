package recipes.service;

import org.springframework.stereotype.Service;
import recipes.dto.RecipeDTO;
import recipes.model.Recipe;
import recipes.repository.RecipeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Long save(RecipeDTO recipeDTO) {
        Recipe recipe = new Recipe();
        recipe.setName(recipeDTO.getName());
        recipe.setDescription(recipeDTO.getDescription());
        recipe.setIngredients(recipeDTO.getIngredients());
        recipe.setDirections(recipeDTO.getDirections());
        recipeRepository.save(recipe);
        return recipe.getId();
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
                    dto.setDescription(recipe.getDescription());
                    dto.setIngredients(recipe.getIngredients());
                    dto.setDirections(recipe.getDirections());
                    return dto;
                })
                .orElse(null); // Return null if not found
    }

    public List<RecipeDTO> findAll() {
        List<Recipe> recipes =  recipeRepository.findAll();

        return convertToDTO(recipes);
    }

    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
    }

    public List<RecipeDTO> convertToDTO(List<Recipe> recipes) {
        List<RecipeDTO>  recipeDTOs = new ArrayList<>();
        for(Recipe createdRecipe : recipes) {
            RecipeDTO createdRecipeDTO = new RecipeDTO();
            createdRecipeDTO.setName(createdRecipe.getName());
            createdRecipeDTO.setDescription(createdRecipe.getDescription());
            createdRecipeDTO.setIngredients(createdRecipe.getIngredients());
            createdRecipeDTO.setDirections(createdRecipe.getDirections());

            recipeDTOs.add(createdRecipeDTO);
        }
        return recipeDTOs;
    }
}
