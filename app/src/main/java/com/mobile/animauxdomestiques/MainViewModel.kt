package com.mobile.animauxdomestiques

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.animauxdomestiques.data.entities.Animal
import com.mobile.animauxdomestiques.data.entities.Specie
import com.mobile.animauxdomestiques.data.entities.activity.Activity
import com.mobile.animauxdomestiques.data.AppDatabase
import com.mobile.animauxdomestiques.data.entities.activity.ActivityConfiguration
import com.mobile.animauxdomestiques.data.entities.activity.GlobalActivity
import com.mobile.animauxdomestiques.data.entities.activity.SpecieActivity
import com.mobile.animauxdomestiques.model.ActivityConfigurationModel
import com.mobile.animauxdomestiques.model.ActivityConfigurationWithAnimal
import com.mobile.animauxdomestiques.model.SearchResult
import com.mobile.animauxdomestiques.model.enums.ActivityLink
import com.mobile.animauxdomestiques.model.enums.Gender
import com.mobile.animauxdomestiques.model.enums.ReminderType
import com.mobile.animauxdomestiques.navigation.Screen
import com.mobile.animauxdomestiques.utils.convertUriToBitmap
import com.mobile.animauxdomestiques.utils.saveImageToInternalStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val animalsDao by lazy { AppDatabase.getDatabase(application).animalsDao() }
    private val speciesDao by lazy { AppDatabase.getDatabase(application).speciesDao() }
    private val activityDao by lazy { AppDatabase.getDatabase(application).activityDao() }

    val searchResultsFlow: Flow<List<SearchResult>> = combine(
        animalsDao.getAllAnimalsFlow(),
        activityDao.getAllActivitiesFlow(),
        speciesDao.getAllSpeciesFlow()
    ) { animals, activities, species ->
        val animalResults = animals.map { animal ->
            SearchResult(
                headLineContent = animal.name,
                supportingContent = animal.breed ?: "Race inconnue",
                type = "Animal",
                action = { navController ->
                    setAnimalSelected(animal)
                    navController.navigate(Screen.AnimalScreen.route)
                }
            )
        }
        val activityResults = activities.map { activity ->
            SearchResult(
                headLineContent = activity.name,
                supportingContent = activity.description ?: "Aucune description",
                type = "Activity",
                action = {navController  ->
                    setActivitySelected(activity)
                    navController.navigate(Screen.ActivityScreen.route)
                }
            )
        }
        val specieResults = species.map { specie ->
            SearchResult(
                headLineContent = specie.name,
                supportingContent = "",
                type = "Specie",
                action = { navController  ->
                    setSpecieSelected(specie)
                    navController.navigate(Screen.SpecieScreen.route)
                }
            )
        }
        animalResults + activityResults + specieResults
    }

    val allAnimalsFlow = animalsDao.getAllAnimalsFlow()

    fun getSpecieById(specieId: Int): Flow<Specie> {
        return speciesDao.getSpecieById(specieId)
    }

    fun getAllSpecies(): Flow<List<Specie>> {
        return speciesDao.getAllSpeciesFlow()
    }

    // Add Animal
    val newAnimalName = mutableStateOf("")
    val newAnimalBreed = mutableStateOf("")
    val newAnimalGender = mutableStateOf(Gender.UNSPECIFIED)
    val newAnimalAge: MutableState<Int?> = mutableStateOf(null)
    val newAnimalWeight: MutableState<Float?> = mutableStateOf(null)
    val specieIdSelected : MutableState<Int?> = mutableStateOf(null)
    var selectedImagePath = mutableStateOf("")

    fun onNewAnimalNameChange(newName: String) {
        newAnimalName.value = newName
    }

    fun onNewAnimalBreedChange(newBreed: String) {
        newAnimalBreed.value = newBreed
    }

    fun setNewAnimalGender(newGender: Gender) {
        newAnimalGender.value = newGender
    }

    fun onNewAnimalAgeChange(newAge: Int?) {
        newAnimalAge.value = newAge
    }

    fun onNewAnimalWeightChange(newWeight: Float?) {
        newAnimalWeight.value = newWeight
    }

    fun setSpecieIdSelected(specieId: Int) {
        specieIdSelected.value = specieId
    }

    val animalSelected: MutableState<Animal?> = mutableStateOf(null)

    fun setAnimalSelected(animal: Animal?) {
        animalSelected.value = animal
        if (animal != null) {
            newAnimalName.value = animal.name
            newAnimalBreed.value = animal.breed.toString()
            newAnimalGender.value = animal.gender!!
            newAnimalAge.value = animal.age
            newAnimalWeight.value = animal.weight
            selectedImagePath.value = animal.imagePath.toString()
            setSpecieIdSelected(animal.specieId)
        }
    }

    var erreurIns = mutableIntStateOf(0)
    var erreurMsg = mutableStateOf("")
    var inputErrorDetected = mutableStateOf(false)

    var showFullScreenDialog = mutableStateOf(false)
    var showDeleteDialog = mutableStateOf(false)

    fun deleteAnimalSelected(){
        viewModelScope.launch(Dispatchers.IO) {
            animalSelected.value?.let {
                val result = animalsDao.deleteAnimal(it)

                if (result == 0) {
                    erreurIns.intValue = 1
                    erreurMsg.value = "La suppression de l'animal a échoué"
                } else {
                    erreurIns.intValue = 2
                    erreurMsg.value = "L'animal a bien été supprimé"
                }
            }
        }
    }

    fun deleteActivitySelected(){
        viewModelScope.launch(Dispatchers.IO) {
            activitySelected.value?.let {
                val result = activityDao.deleteActivity(it)
                if (result == 0) {
                    erreurIns.intValue = 1
                    erreurMsg.value = "La suppression de l'activité a échoué"
                } else {
                    erreurIns.intValue = 2
                    erreurMsg.value = "L'activité a bien été supprimée"
                }
            }
        }
    }

    fun deleteSpecieSelected(){
        viewModelScope.launch(Dispatchers.IO) {
            specieSelected.value?.let {
                val result = speciesDao.deleteSpecie(it)

                if (result == 0) {
                    erreurIns.intValue = 1
                    erreurMsg.value = "La suppression de l'espèce a échoué"
                } else {
                    erreurIns.intValue = 2
                    erreurMsg.value = "L'espèce a bien été supprimée"
                }
            }
        }
    }

    private fun checkAnimalInputAreValid():Boolean{
        return (newAnimalName.value.trim() != "" && specieIdSelected.value != null)
    }

    fun clearAnimalInput(){
        newAnimalName.value = ""
        newAnimalBreed.value = ""
        newAnimalGender.value = Gender.UNSPECIFIED
        newAnimalAge.value = null
        newAnimalWeight.value = null
        selectedImagePath.value = ""
        specieIdSelected.value = null
    }

    fun addAnimal(context: Context) {
        if (checkAnimalInputAreValid()) {
            viewModelScope.launch(Dispatchers.IO) {
                val bitmap = convertUriToBitmap(context, selectedImagePath.value.toUri())

                val imagePath = bitmap?.let {
                    saveImageToInternalStorage(context, it, "animal_${System.currentTimeMillis()}")
                }

                val result = animalsDao.insertAnimal(
                    Animal(
                        name = newAnimalName.value,
                        breed = newAnimalBreed.value,
                        gender = newAnimalGender.value,
                        age = newAnimalAge.value,
                        weight = newAnimalWeight.value,
                        imagePath = imagePath,
                        specieId = specieIdSelected.value!!
                    )
                )

                if (result == -1L) {
                    erreurIns.intValue = 1
                    erreurMsg.value = "L'ajout de l'animal a échoué"
                } else {
                    erreurIns.intValue = 2
                    erreurMsg.value = "L'animal a bien été ajouté"
                    initActivityOfAnimal(result.toInt(), specieIdSelected.value!!)
                }
            }
        } else {
            inputErrorDetected.value = true
        }
    }

    fun updateAnimal(context: Context){
        if (checkAnimalInputAreValid()) {
            viewModelScope.launch(Dispatchers.IO) {
                if (animalSelected.value != null) {
                    val bitmap = convertUriToBitmap(context, selectedImagePath.value.toUri())

                    val imagePath = bitmap?.let {
                        saveImageToInternalStorage(context, it, "animal_${System.currentTimeMillis()}")
                    }
                    val updatedAnimal = Animal(
                        animalId = animalSelected.value!!.animalId,
                        name = newAnimalName.value,
                        breed = newAnimalBreed.value,
                        gender = newAnimalGender.value,
                        age = newAnimalAge.value,
                        weight = newAnimalWeight.value,
                        imagePath = imagePath,
                        specieId = specieIdSelected.value!!
                    )
                    val result = animalsDao.updateAnimal(updatedAnimal)

                    if (result == 0) {
                        erreurIns.intValue = 1
                        erreurMsg.value =
                            "L'animal n'a pas pu être mis à jour (peut-être déjà à jour ou non trouvé)"
                    }
                    else {
                        erreurIns.intValue = 2
                        erreurMsg.value = "L'animal a bien été mis à jour"
                        setAnimalSelected(updatedAnimal)
                    }
                }
            }
        } else {
            inputErrorDetected.value = true
        }
    }

    private fun initActivityOfAnimal(animalId: Int, specieId: Int?) {
        val globalActivityList = this.getGlobalActivityList()

        val activityConfigurationList: MutableList<ActivityConfiguration> = mutableListOf()

        for (globalActivity in globalActivityList) {
            activityConfigurationList.add(
                ActivityConfiguration(
                    animalId = animalId,
                    activityId = globalActivity.activityId
                )
            )
        }

        if (specieId != null){
            val specieActivityList = this.getActivityListFromSpecieId(specieId)
            for (specieActivity in specieActivityList) {
                activityConfigurationList.add(
                    ActivityConfiguration(
                        animalId = animalId,
                        activityId = specieActivity.activityId
                    )
                )
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            activityDao.insertActivityConfigurationList(
                activityConfigurationList = activityConfigurationList
            )
        }
    }

    private fun getActivityListFromSpecieId(specieId: Int): List<Activity> {
        return activityDao.getAllActivitiesForSpecieAsList(specieId)
    }

    private fun getGlobalActivityList(): List<Activity> {
        return activityDao.getGlobalActivitiesAsList()
    }

    fun getActivityFromAnimal(animalId: Int): Flow<List<ActivityConfigurationModel>> {
        return activityDao.getAllActivitiesForAnimal(animalId)
    }

    val allActivitiesFlow = activityDao.getAllActivitiesFlow()

    var activityConfigurationSelected: MutableState<ActivityConfigurationModel?> =
        mutableStateOf(null)

    fun setActivityConfigurationFormSelected(activityConfigurationModel: ActivityConfigurationModel?) {
        this.activityConfigurationSelected.value = activityConfigurationModel
    }

    fun updateActivityConfiguration(
        activityConfigurationId: Int,
        reminderType: ReminderType,
        dateTime: LocalDateTime
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            activityDao.updateActivityConfiguration(
                dateTime.atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli(),
                reminderType,
                activityConfigurationId
            )
        }
    }

    // Activity
    val newActivityName = mutableStateOf("")

    fun onNewActivityNameChange(newName: String) {
        newActivityName.value = newName
    }

    val newActivityDescription = mutableStateOf("")

    fun onNewActivityDescriptionChange(newDescription: String) {
        newActivityDescription.value = newDescription
    }

    var selectedSpecies = mutableStateListOf<Specie>()

    fun updateSelectedSpecies(species: List<Specie>) {
        selectedSpecies.clear()
        selectedSpecies.addAll(species)
    }

    var selectedAnimals = mutableStateListOf<Animal>()

    fun updateSelectedAnimals(animals: List<Animal>) {
        selectedAnimals.clear()
        selectedAnimals.addAll(animals)
    }

    var activitySelected: MutableState<Activity?> =  mutableStateOf(null)

    fun setActivitySelected(activity: Activity?) {
        selectedSpecies.clear()
        selectedAnimals.clear()
        selectedOptions.clear()
        selectedOptions.addAll(listOf(false,false,false))
        this.activitySelected.value = activity
        if (activity != null) {
            newActivityName.value = activity.name
            newActivityDescription.value = activity.description.toString()
        }
    }

    fun clearActivityInput(){
        setActivitySelected(null)
        newActivityName.value = ""
        newActivityDescription.value = ""
    }

    suspend fun getSpeciesByActivityId(activityId: Int): List<Specie> {
        return activityDao.getSpeciesByActivityId(activityId)
    }

    suspend fun getAnimalsByActivityId(activityId: Int): List<Animal> {
        return activityDao.getAnimalsByActivityId(activityId)
    }

    suspend fun isGlobalActivity(activityId: Int): Boolean {
        return activityDao.isGlobalActivity(activityId)
    }

    fun getActivityConfigurationFlow(activityId : Int?): Flow<List<ActivityConfigurationWithAnimal>> {
        return activityDao.getAllActivitiesConfigurationFlowByActivity(
            activityId
        )
    }

    private fun checkActivityInputAreValid():Boolean{
        return (newActivityName.value.trim() != "")
    }

    var selectedOptions = mutableStateListOf(
        false,
        false,
        false
    )

    fun addActivity() {
        if (checkActivityInputAreValid()) {
            viewModelScope.launch(Dispatchers.IO) {
                val activity = Activity(
                    name = newActivityName.value,
                    description = newActivityDescription.value
                )
                val activityIdLong = activityDao.insertActivity(activity)
                if (activityIdLong == -1L) {
                    erreurIns.intValue = 1
                    erreurMsg.value = "L'ajout de l'activité a échoué"
                } else {
                    val activityId = activityIdLong.toInt()
                    val animalsToInit = selectedAnimals
                    var insertionErrorDetected = false
                    if (selectedOptions[ActivityLink.DEFAULT.ordinal]) {
                        val globalActivityIdLong = activityDao.insertGlobalActivity(
                            GlobalActivity(activityId)
                        )
                        if (globalActivityIdLong == -1L) {
                            insertionErrorDetected = true
                        } else {
                            animalsToInit.addAll(animalsDao.getAllAnimalsList())
                        }
                    }
                    if (selectedOptions[ActivityLink.SPECIE.ordinal]) {
                        selectedSpecies.forEach { specie ->
                            val specieActivityIdLong = activityDao.insertSpecieActivity(
                                SpecieActivity(
                                    activityId = activityId,
                                    specieId = specie.specieId
                                )
                            )
                            if (specieActivityIdLong == -1L) {
                                insertionErrorDetected = true
                            } else {
                                animalsToInit.addAll(animalsDao.getAllAnimalsListBySpecieId(specie.specieId))
                            }
                        }
                    }
                    if (selectedOptions[ActivityLink.ANIMAL.ordinal]) {
                        animalsToInit.addAll(selectedAnimals)
                    }

                    if(!insertionErrorDetected){
                        animalsToInit.forEach { animal ->
                            activityDao.insertActivityConfiguration(
                                ActivityConfiguration(
                                    activityId = activityId,
                                    animalId = animal.animalId
                                )
                            )
                        }
                    }
                    else{
                        erreurIns.intValue = 1
                        erreurMsg.value = "L'ajout de l'activité a échoué"
                        activityDao.deleteActivity(activity)
                    }
                }
            }
        } else {
            inputErrorDetected.value = true
        }
    }

    fun updateCurrentActivity(){
        activitySelected.value?.let { updateActivity(it) }
    }

    private fun updateActivity(activity: Activity) {
        if (checkActivityInputAreValid()) {
            viewModelScope.launch(Dispatchers.IO) {
                val updatedActivity = Activity(
                    activityId = activity.activityId,
                    newActivityName.value,
                    newActivityDescription.value
                )

                val result = activityDao.updateActivity(updatedActivity)
                if (result == 0) {
                    erreurIns.intValue = 1
                    erreurMsg.value = "La mise à jour de l'activité a échoué"
                } else {
                    val animalsToUpdate = mutableListOf<Animal>()
                    var updateErrorDetected = false

                    // DEFAULT
                    if (selectedOptions[ActivityLink.DEFAULT.ordinal]) {
                        var globalActivityIdLong : Long = 0
                        if(!activityDao.isGlobalActivity(activity.activityId)) {
                            globalActivityIdLong = activityDao.insertGlobalActivity(
                                GlobalActivity(activity.activityId)
                            )
                        }
                        if (globalActivityIdLong == -1L) {
                            updateErrorDetected = true
                        } else {
                            animalsToUpdate.addAll(animalsDao.getAllAnimalsList())
                        }
                    }
                    else {
                        if (activityDao.isGlobalActivity(activity.activityId)) {
                            activityDao.deleteGlobalActivityByActivityId(activity.activityId)
                        }
                    }

                    // SPECIE

                    if (!selectedOptions[ActivityLink.SPECIE.ordinal]) {
                        selectedSpecies.clear()
                        if (activityDao.isSpecieActivity(activity.activityId)) {
                            activityDao.deleteSpecieActivityByActivityId(activity.activityId)
                        }
                    }

                    selectedSpecies.forEach { specie ->
                        if (!selectedOptions[ActivityLink.SPECIE.ordinal]) {
                            activityDao.deleteSpecieActivity(activity.activityId, specie.specieId)
                        } else {
                            var specieActivityIdLong : Long = 0
                            if (!activityDao.isSpecieActivity(activity.activityId)) {
                                specieActivityIdLong = activityDao.insertSpecieActivity(
                                    SpecieActivity(
                                        activityId = activity.activityId,
                                        specieId = specie.specieId
                                    )
                                )
                            }
                            if (specieActivityIdLong == -1L) {
                                updateErrorDetected = true
                            } else {
                                animalsToUpdate.addAll(animalsDao.getAllAnimalsListBySpecieId(specie.specieId))
                            }
                        }
                    }

                    if (selectedOptions[ActivityLink.ANIMAL.ordinal]) {
                        animalsToUpdate.addAll(selectedAnimals)
                    }
                    else{
                        selectedAnimals.clear()
                    }

                    // DELETE OLD CONFIGURATIONS
                    val existingActivityConfigurationWithAnimal = activityDao
                        .getAllActivitiesConfigurationByActivity(activity.activityId)
                    val animalsToRemove = existingActivityConfigurationWithAnimal.filter { configWithAnimal ->
                        animalsToUpdate.none { it.animalId == configWithAnimal.animalId }
                    }
                    animalsToRemove.forEach { activityConfig ->
                        activityDao.deleteActivityConfigurationByActivityId(activityConfig.activityId)
                    }

                    if (!updateErrorDetected) {
                        animalsToUpdate.forEach { animal ->
                            val activityConfig = ActivityConfiguration(
                                activityId = activity.activityId,
                                animalId = animal.animalId
                            )
                            setActivitySelected(updatedActivity)
                            activityDao.insertActivityConfiguration(activityConfig)
                        }
                    } else {
                        // Si une erreur a été détectée, annuler la mise à jour
                        erreurIns.intValue = 1
                        erreurMsg.value = "La mise à jour de l'activité a échoué"
                    }
                }
            }
        } else {
            inputErrorDetected.value = true
        }
    }

    // Specie

    val specieSelected: MutableState<Specie?> = mutableStateOf(null)

    fun setSpecieSelected(specie: Specie?){
        specieSelected.value = specie
        if (specie != null) {
            newSpecieName.value = specie.name
        }
    }

    // Add Specie

    val newSpecieName = mutableStateOf("")

    fun onNewSpecieNameChange(newName: String) {
        newSpecieName.value = newName
    }

    fun addSpecie() {
        if (newSpecieName.value.trim() != "") {
            viewModelScope.launch(Dispatchers.IO) {
                val result = speciesDao.insertSpecie(
                    Specie(
                        name = newSpecieName.value,
                    )
                )

                if (result == -1L) {
                    erreurIns.intValue = 1
                    erreurMsg.value = "L'ajout de l'espèce a échoué"
                } else {
                    erreurIns.intValue = 2
                    erreurMsg.value = "L'espèce a bien été ajouté"
                }
            }
        } else {
            inputErrorDetected.value = true
        }
    }

    fun clearSpecieInput(){
        newSpecieName.value = ""
        specieSelected.value = null
    }

    fun getAllAnimalsFlowBySpecieId(specieId: Int?): Flow<List<Animal>> {
        return animalsDao.getAllAnimalsFlowBySpecieId(specieId)
    }

    fun updateCurrentSpecie(){
        specieSelected.value?.let { updateSpecie(it) }
    }

    private fun updateSpecie(specie: Specie) {
        if (newSpecieName.value.trim() != "") {
            viewModelScope.launch(Dispatchers.IO) {
                val updatedSpecie = Specie(
                    specieId = specie.specieId,
                    name = newSpecieName.value,
                )

                val result = speciesDao.updateSpecie(updatedSpecie)

                if (result == 0) {
                    erreurIns.intValue = 1
                    erreurMsg.value = "La mise à jour de l'espèce a échoué"
                } else {
                    setSpecieSelected(updatedSpecie)
                    erreurIns.intValue = 2
                    erreurMsg.value = "L'espèce a bien été mise à jour"
                }
            }
        } else {
            inputErrorDetected.value = true
        }
    }

    // LOAD DEFAULT

    fun loadDefaultSpecies() {
        viewModelScope.launch(Dispatchers.IO) {
            val speciesList = speciesDao.getAllSpeciesFlow().firstOrNull()

            if (speciesList.isNullOrEmpty()) {
                speciesDao.insertSpecie(Specie(name = "Canidé"))
                speciesDao.insertSpecie(Specie(name = "Félin"))
                speciesDao.insertSpecie(Specie(name = "Rongeur"))
                speciesDao.insertSpecie(Specie(name = "Poisson"))
            }
        }
    }

}