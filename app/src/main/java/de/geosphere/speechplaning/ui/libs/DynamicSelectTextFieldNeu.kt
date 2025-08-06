package de.geosphere.speechplaning.ui.libs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction

/**
 * A composable function that creates a dynamic select text field with a dropdown menu.
 *
 * This component displays a text field that, when clicked, reveals a dropdown menu with
 * a list of selectable options. The selected option is then displayed in the text field.
 *
 * @param selectedValue The currently selected value to be displayed in the text field.
 * @param options A list of string options to be displayed in the dropdown menu.
 * @param label The label to be displayed above the text field.
 * @param onValueChangedEvent A callback function that is triggered when a new option is selected.
 *        It provides the index of the selected option in the `options` list and the selected option String.
 * @param modifier Modifier to be applied to the container of the component.
 * @param readOnly Controls the editability of the text field. Defaults to true, which prevents the keyboard from
 * appearing.
 *
 * Example Usage:
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicSelectTextFieldNeu(
    selectedValue: String,
    options: List<String>,
    label: String,
    onValueChangedEvent: (Int, String) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = true // Standardmäßig auf true gesetzt
) {
    var expanded by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
            // Fokus löschen, wenn das Menü geöffnet oder geschlossen wird,
            // um sicherzustellen, dass die Tastatur nicht erscheint.
            if (expanded) {
                focusManager.clearFocus()
            }
        },
        modifier = modifier
    ) {
        OutlinedTextField(
            readOnly = readOnly, // Wichtig: Auf true setzen, um die Tastatur zu verhindern
            value = selectedValue,
            onValueChange = {}, // Muss vorhanden sein, auch wenn readOnly true ist
            label = { Text(text = label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = OutlinedTextFieldDefaults.colors(),
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable, true) // Ermöglicht das Anklicken des Textfelds zum Öffnen des Menüs
                .fillMaxWidth()
                .clickable(enabled = !readOnly) { // Nur klicken, wenn nicht readOnly (optional, aber gute Praxis)
                    // Optional: Manuelles Öffnen/Schließen des Menüs hier, falls gewünscht
                    // expanded = !expanded
                    // Fokus löschen, um sicherzustellen, dass die Tastatur ausgeblendet wird
                    focusManager.clearFocus()
                },
            keyboardActions = KeyboardActions( // Tastaturaktionen, falls readOnly false ist
                onAny = {
                    keyboardController?.hide()
                    focusManager.clearFocus() // Fokus entfernen, um die Tastatur zu schließen
                }
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.None // Keine spezifische Aktion für die Enter-Taste
            ),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        expanded = false
                        onValueChangedEvent(index, option)
                        focusManager.clearFocus() // Fokus nach Auswahl entfernen
                    }
                )
            }
        }
    }
}