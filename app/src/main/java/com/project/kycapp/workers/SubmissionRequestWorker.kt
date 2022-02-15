package com.project.kycapp.workers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.project.kycapp.MainActivity
import com.project.kycapp.R
import com.project.kycapp.repository.KycRepository
import com.project.kycapp.utils.Notifier
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.File

@HiltWorker
class SubmissionRequestWorker @AssistedInject constructor(@Assisted appContext: Context, @Assisted params: WorkerParameters, kycRepository: KycRepository) :
    CoroutineWorker(appContext, params) {
    companion object {
        const val KEY_IMAGE_URI: String = "key-submission-uri"
        const val KEY_UPLOADED_URI: String = "key-submitted-uri"
        const val WORK_NAME = "com.project.kycapp.workers.ImageUploadWorker"
    }

    private val context = applicationContext

    private var title: String = "File submission"

    override suspend fun doWork(): Result {
        val fileUri = Uri.fromFile(File(inputData.getString(KEY_IMAGE_URI)))

        fileUri?.let { uri ->
//            repository.upload(uri) { result, percentage ->
//
//            }
        }

        return Result.success()
    }

    private fun showProgressNotification(caption: String, percent: Int) {
        Notifier
            .progressable(
                context,
                100, percent
            ) {
                notificationId = title.hashCode()
                contentTitle = title
                contentText = caption
                smallIcon = R.drawable.ic_baseline_expand_more_24
            }
    }

    private fun showUploadFinishedNotification(downloadUrl: Uri?) {
        Notifier
            .dismissNotification(context, title.hashCode())

        if (downloadUrl != null) return

        val caption = context.getString(
            R.string.error
        )

        val intent = Intent(applicationContext, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0 /* requestCode */, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        Notifier.show(context) {
            notificationId = title.hashCode()
            contentTitle = title
            contentText = caption
            this.pendingIntent = pendingIntent
        }
    }
}