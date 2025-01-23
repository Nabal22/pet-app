package com.mobile.animauxdomestiques.ui.components.animal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AnimalAttributeCard(title:String, data:String?, unit:String? = null, modifier: Modifier){
    Column (
        modifier = modifier
            .padding(5.dp)
            .background(
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .padding(bottom = 5.dp),
            text = title,
            color = MaterialTheme.colorScheme.tertiaryContainer,
            fontSize = MaterialTheme.typography.labelSmall.fontSize
        )
        Row {
            if (data != null) {
                Text(
                    text = data,
                    color = MaterialTheme.colorScheme.onTertiary,
                    fontSize = MaterialTheme.typography.labelMedium.fontSize,
                    maxLines = 1
                )
                if (unit !=null){
                    Text(
                        text = unit,
                        color = MaterialTheme.colorScheme.onTertiary,
                        fontSize = MaterialTheme.typography.labelMedium.fontSize
                    )
                }
            }
            else{
                Text(
                    text = "Iconnu",
                    color = MaterialTheme.colorScheme.onTertiary,
                    fontSize = MaterialTheme.typography.labelMedium.fontSize,
                    maxLines = 1
                )
            }
        }
    }
}