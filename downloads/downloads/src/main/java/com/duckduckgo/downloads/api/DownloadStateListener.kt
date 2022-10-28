package com.duckduckgo.downloads.api

import kotlinx.coroutines.flow.Flow

/** Interface containing download callbacks. */
interface DownloadStateListener {
    /**
     * Data stream that sequentially emits commands of type [DownloadCommand].
     */
    fun commands(): Flow<DownloadCommand>
}
