package com.mobile.animauxdomestiques.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mobile.animauxdomestiques.model.SearchResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    completeList: List<SearchResult>,
    placeholder: @Composable () -> Unit,
    getIconFromType:  (type : String) -> ImageVector,
    navHostController: NavHostController
) {
    var query by remember { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    val filteredList = remember(query, completeList) {
        completeList.filter {
            it.headLineContent.contains(query, ignoreCase = true) ||
                    it.supportingContent.contains(query, ignoreCase = true)
        }
    }

    Box(Modifier.fillMaxSize().semantics { isTraversalGroup = true }) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f },
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = { query = it },
                    onSearch = { expanded = false },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = placeholder,
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                repeat(filteredList.size) { idx ->
                    val resultText = filteredList[idx]
                    ListItem(
                        headlineContent = { Text(resultText.headLineContent) },
                        supportingContent = {
                            if (resultText.supportingContent.isNotEmpty()) {
                                Text(resultText.supportingContent)
                            }
                        },
                        leadingContent = { Icon( getIconFromType(resultText.type), contentDescription = null) },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier =
                        Modifier.clickable {
                            expanded = false
                            resultText.action(navHostController)
                        }
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                    if (idx != filteredList.size-1) HorizontalDivider()
                }
                if (filteredList.isEmpty()){
                    Text(
                        modifier = Modifier.fillMaxSize().padding(15.dp),
                        text= "Aucun élémént ne correspond à votre recherche..."
                    )
                }
            }
        }
    }
}
