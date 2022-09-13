/*
 * Copyright (c) 2022 DuckDuckGo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duckduckgo.lint.ui

import com.android.tools.lint.checks.infrastructure.TestFile
import com.android.tools.lint.checks.infrastructure.TestFiles
import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask
import com.duckduckgo.lint.ui.NoDesignSystemDialogDetector.Companion.NO_DESIGN_SYSTEM_DIALOG
import org.junit.Test

class NoDesignSystemDialogDetectorTest {

    @Test
    fun whenAlertDialogisShownThenFailWithError() {
        TestLintTask.lint()
            .files(
                TestFiles.kt(
                    """
              package com.duckduckgo.lint.ui
    
                import androidx.appcompat.app.AlertDialog

                class RandomActivity {

                    fun createDialog() {                    
                        AlertDialog.Builder(context)
                            .setTitle("Title")
                            .setMessage("Message")
                            .setPositiveButton("Accept") { _, _ ->
                                onSettingsLaunchSelected()
                            }
                            .setNegativeButton("Deny") { _, _ ->
                                onSettingsLaunchDeclined()
                            }
                            .show()
                    }
                }
            """
                ).indented(), alertDialog
            )
            .issues(NO_DESIGN_SYSTEM_DIALOG)
            .allowCompilationErrors()
            .run()
            .expect(
                """
                src/com/duckduckgo/lint/Fragment.kt:5: Error: Fragment should not be directly extended [NoFragment]
                  class Duck : Fragment() {
                        ~~~~
                1 errors, 0 warnings
            """.trimMargin()
            )
    }

    @Test
    fun whenAlertDialogReferencesIsShownThenFailWithError() {
        TestLintTask.lint()
            .files(
                TestFiles.kt(
                    """
              package com.duckduckgo.lint.ui
    
                class Fragment {

                    fun createDialog() {                    
                        val editDialog = AlertDialog.Builder(this).apply {
                            setTitle(R.string.dialogEditTitle)
                            setView(dialogBinding.root)
                            setPositiveButton(R.string.dialogSaveAction) { _, _ ->
                                val newText = dialogBinding.textInput.text.toString()
                                viewModel.onEntryEdited(entry, UserWhitelistedDomain(newText))
                            }
                            setNegativeButton(android.R.string.no) { _, _ -> }
                        }.create()
                        
                        dialog?.dismiss()
                        dialog = editDialog
                        editDialog.show()
                    }
                }
            """
                ).indented(), alertDialog
            )
            .allowCompilationErrors()
            .issues(NO_DESIGN_SYSTEM_DIALOG)
            .run()
            .expect(
                """
                src/com/duckduckgo/lint/Fragment.kt:5: Error: Fragment should not be directly extended [NoFragment]
                  class Duck : Fragment() {
                        ~~~~
                1 errors, 0 warnings
            """.trimMargin()
            )
    }

    private val alertDialog: TestFile = java(
        """
            package androidx.appcompat.app;
            
            @SuppressWarnings("all") // stubs
            public class AlertDialog {
                public static class Builder {
                    public Builder(Context context) {   
                    
                        public AlertDialog show() {
                            return null;
                        }
                        
                        public AlertDialog create() {
                            return null;
                        }
                    }
                  }
            }
        """
    ).indented()
}


