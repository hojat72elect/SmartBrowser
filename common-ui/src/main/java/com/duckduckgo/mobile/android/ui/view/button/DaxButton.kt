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

package com.duckduckgo.mobile.android.ui.view.button

import android.content.Context
import android.util.AttributeSet
import com.duckduckgo.mobile.android.R
import com.google.android.material.button.MaterialButton

class DaxButton @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : MaterialButton(
    ctx,
    attrs,
    defStyleAttr
) {

    init {
        val typedArray =
            context.obtainStyledAttributes(
                attrs,
                R.styleable.DaxButton,
                0,
                0
            )

        val buttonType = if (typedArray.hasValue(R.styleable.DaxButton_buttonType)) {
            ButtonType.from(typedArray.getInt(R.styleable.DaxButton_buttonType, 0))
        } else {
            ButtonType.Primary
        }

        val isSmallButton = if (typedArray.hasValue(R.styleable.DaxButton_buttonSize)) {
            typedArray.getInt(R.styleable.DaxButton_buttonSize, 0) == 0
        } else {
            false
        }

        setButtonHeight(isSmallButton)

        typedArray.recycle()
    }

    private fun setButtonHeight(isSmall: Boolean) {
        if (isSmall) {
            minHeight = resources.getDimensionPixelOffset(R.dimen.buttonSmallHeight)
        } else {
            minHeight = resources.getDimensionPixelOffset(R.dimen.buttonLargeHeight)
        }
    }

    enum class ButtonType {
        Primary,
        Secondary,
        Ghost,
        Destructive,
        DestructiveGhost;

        companion object {
            fun from(type: Int): ButtonType {
                // same order as attrs-typography.xml
                return when (type) {
                    0 -> Primary
                    1 -> Secondary
                    2 -> Ghost
                    3 -> Destructive
                    4 -> DestructiveGhost
                    else -> Primary
                }
            }

            fun getButtonStyle(type: ButtonType): Int {
                return when (type) {
                    Primary -> R.style.Widget_DuckDuckGo_DaxButton_Ghost
                    Secondary -> R.style.Widget_DuckDuckGo_DaxButton_Ghost
                    Ghost -> R.style.Widget_DuckDuckGo_DaxButton_Ghost
                    Destructive -> R.style.Widget_DuckDuckGo_DaxButton_Ghost
                    DestructiveGhost -> R.style.Widget_DuckDuckGo_DaxButton_Ghost
                }
            }
        }
    }
}



