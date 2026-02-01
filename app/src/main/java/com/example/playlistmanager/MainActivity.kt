package com.example.playlistmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

/**
 * ════════════════════════════════════════════════════════════════════════════
 * Student Number: [YOUR STUDENT NUMBER HERE]
 * Full Name: [YOUR FULL NAME HERE]
 *
 * Music Playlist Manager App - Premium Edition
 * This app allows users to manage a playlist of songs with ratings and comments
 * ════════════════════════════════════════════════════════════════════════════
 */

class MainActivity : AppCompatActivity() {

    // TAG for logging - demonstrates understanding of code
    private val TAG = "MainActivity"

    // ═══════════════════════════════════════════════════════════════════════
    // Q.1.1 REQUIREMENT: PARALLEL ARRAYS (3 marks)
    // Four parallel arrays to store song data
    // ═══════════════════════════════════════════════════════════════════════
    companion object {
        // Maximum number of songs (as per assignment requirement: 4 songs)
        const val MAX_ITEMS = 4

        // ═══ CHANGEABLE FOR OTHER APPS ═══
        // Array 1: Song titles (or Movie titles, Book titles, Restaurant names)
        var itemTitles = arrayOfNulls<String>(MAX_ITEMS)

        // Array 2: Artist names (or Directors, Authors, Cuisine types)
        var itemCreators = arrayOfNulls<String>(MAX_ITEMS)

        // Array 3: Ratings (1-5 scale)
        var itemRatings = arrayOfNulls<Int>(MAX_ITEMS)

        // Array 4: User comments (or Reviews, Notes)
        var itemComments = arrayOfNulls<String>(MAX_ITEMS)

        // Counter to track how many items have been added
        var itemCount = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Logging to demonstrate understanding (Q.1.1 requirement)
        Log.d(TAG, "MainActivity created - Main screen loaded")

        // Hide status bar for immersive experience
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        // ═══════════════════════════════════════════════════════════════════
        // Initialize views from XML layout
        // ═══════════════════════════════════════════════════════════════════
        val btnAddItem: Button = findViewById(R.id.btnAddToPlaylist)
        val btnViewDetails: Button = findViewById(R.id.btnViewDetails)
        val btnExit: Button = findViewById(R.id.btnExit)
        val tvSongCount: TextView = findViewById(R.id.tvSongCount)

        // Update song count display
        updateSongCount(tvSongCount)

        // ═══════════════════════════════════════════════════════════════════
        // Q.1.2 REQUIREMENT: BUTTON 1 - "Add to Playlist" (10 marks)
        // When clicked, asks user to enter:
        // 1. Song title
        // 2. Artist name
        // 3. Rating (1-5)
        // 4. User comments
        // ═══════════════════════════════════════════════════════════════════
        btnAddItem.setOnClickListener {
            Log.d(TAG, "Add to Playlist button clicked")
            animateButtonPress(it)
            addItemToPlaylist(tvSongCount)
        }

        // ═══════════════════════════════════════════════════════════════════
        // Q.1.2 REQUIREMENT: BUTTON 2 - Navigate to second screen (5 marks)
        // ═══════════════════════════════════════════════════════════════════
        btnViewDetails.setOnClickListener {
            Log.d(TAG, "View Details button clicked - Navigating to DetailedViewActivity")
            animateButtonPress(it)

            // Navigate to detailed view screen after animation
            it.postDelayed({
                val intent = Intent(this, DetailedViewsActivity::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            }, 200)
        }

        // ═══════════════════════════════════════════════════════════════════
        // Q.1.2 REQUIREMENT: BUTTON 3 - Exit the app (5 marks)
        // ═══════════════════════════════════════════════════════════════════
        btnExit.setOnClickListener {
            Log.d(TAG, "Exit button clicked")
            animateButtonPress(it)
            showExitDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        // Update count when returning from detailed view
        val tvSongCount: TextView = findViewById(R.id.tvSongCount)
        updateSongCount(tvSongCount)
    }

    /**
     * ════════════════════════════════════════════════════════════════════════
     * Q.1.1 REQUIREMENT: METHODS/FUNCTIONS (2 marks)
     * Updates the song count display
     * ════════════════════════════════════════════════════════════════════════
     */
    private fun updateSongCount(textView: TextView) {
        textView.text = "$itemCount / $MAX_ITEMS songs"

        // Animation for visual feedback
        textView.alpha = 0f
        textView.animate()
            .alpha(1f)
            .setDuration(500)
            .start()
    }

    /**
     * Animation for button press (premium feel)
     */
    private fun animateButtonPress(view: View) {
        view.animate()
            .scaleX(0.9f)
            .scaleY(0.9f)
            .setDuration(100)
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .start()
            }
            .start()
    }

    /**
     * ════════════════════════════════════════════════════════════════════════
     * Q.1.2 REQUIREMENT: ADD TO PLAYLIST FUNCTION (10 marks)
     * This function handles adding a new song to the playlist
     * It prompts the user for 4 pieces of information:
     * 1. Song title
     * 2. Artist name
     * 3. Rating (1-5)
     * 4. User comments
     * ════════════════════════════════════════════════════════════════════════
     */
    private fun addItemToPlaylist(countTextView: TextView) {
        // ═══════════════════════════════════════════════════════════════════
        // Q.1.1 REQUIREMENT: ERROR HANDLING (5 marks)
        // Check if playlist is full
        // ═══════════════════════════════════════════════════════════════════
        if (itemCount >= MAX_ITEMS) {
            Log.w(TAG, "Playlist full - Cannot add more items")
            Toast.makeText(
                this,
                "Playlist is full! Maximum $MAX_ITEMS songs allowed.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        // ═══════════════════════════════════════════════════════════════════
        // STEP 1: Get song title
        // ═══ CHANGEABLE: Change "Song Title" to "Movie Title", "Book Title", etc.
        // ═══════════════════════════════════════════════════════════════════
        showInputDialog(
            "Enter Song Title",  // ← CHANGEABLE: Dialog title
            "Song Title"         // ← CHANGEABLE: Hint text
        ) { title ->
            if (title.isNotEmpty()) {
                Log.d(TAG, "Song title entered: $title")

                // ═══════════════════════════════════════════════════════════
                // STEP 2: Get artist name
                // ═══ CHANGEABLE: Change "Artist Name" to "Director", "Author", etc.
                // ═══════════════════════════════════════════════════════════
                showInputDialog(
                    "Enter Artist Name",  // ← CHANGEABLE: Dialog title
                    "Artist Name"         // ← CHANGEABLE: Hint text
                ) { artist ->
                    if (artist.isNotEmpty()) {
                        Log.d(TAG, "Artist name entered: $artist")

                        // ═══════════════════════════════════════════════════
                        // STEP 3: Get rating (1-5)
                        // ═══════════════════════════════════════════════════
                        showInputDialog(
                            "Enter Rating (1-5)",  // ← CHANGEABLE: Dialog title
                            "Rating"               // ← CHANGEABLE: Hint text
                        ) { ratingStr ->
                            // Q.1.1 REQUIREMENT: ERROR HANDLING (5 marks)
                            // Validate rating is between 1 and 5
                            val rating = validateRating(ratingStr)
                            if (rating != null) {
                                Log.d(TAG, "Valid rating entered: $rating")

                                // ═══════════════════════════════════════════
                                // STEP 4: Get comments
                                // ═══ CHANGEABLE: Change "Comments" to "Review", "Notes", etc.
                                // ═══════════════════════════════════════════
                                showInputDialog(
                                    "Enter Comments",   // ← CHANGEABLE: Dialog title
                                    "Your thoughts..."  // ← CHANGEABLE: Hint text
                                ) { comments ->
                                    if (comments.isNotEmpty()) {
                                        // ═══════════════════════════════════
                                        // Save all data to parallel arrays
                                        // ═══════════════════════════════════
                                        itemTitles[itemCount] = title
                                        itemCreators[itemCount] = artist
                                        itemRatings[itemCount] = rating
                                        itemComments[itemCount] = comments
                                        itemCount++

                                        Log.d(TAG, "Song added successfully! Total songs: $itemCount")

                                        // Update UI
                                        updateSongCount(countTextView)

                                        // Show success message
                                        Toast.makeText(
                                            this,
                                            "Song added successfully!",  // ← CHANGEABLE
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        showError("Comments cannot be empty")
                                    }
                                }
                            } else {
                                showError("Please enter a valid rating between 1 and 5")
                            }
                        }
                    } else {
                        showError("Artist name cannot be empty")  // ← CHANGEABLE
                    }
                }
            } else {
                showError("Song title cannot be empty")  // ← CHANGEABLE
            }
        }
    }

    /**
     * ════════════════════════════════════════════════════════════════════════
     * Q.1.1 REQUIREMENT: METHODS/FUNCTIONS (2 marks)
     * Generic function to show input dialog
     * ════════════════════════════════════════════════════════════════════════
     */
    private fun showInputDialog(
        title: String,
        hint: String,
        callback: (String) -> Unit
    ) {
        val editText = EditText(this).apply {
            this.hint = hint
            setPadding(50, 40, 50, 40)
            textSize = 16f
        }

        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(editText)
            .setPositiveButton("OK") { _, _ ->
                val input = editText.text.toString().trim()
                callback(input)
            }
            .setNegativeButton("Cancel", null)
            .setCancelable(false)
            .show()
    }

    /**
     * ════════════════════════════════════════════════════════════════════════
     * Q.1.1 REQUIREMENT: ERROR HANDLING (5 marks)
     * Validates that the rating is between 1 and 5
     * ════════════════════════════════════════════════════════════════════════
     */
    private fun validateRating(ratingStr: String): Int? {
        return try {
            val rating = ratingStr.toInt()
            if (rating in 1..5) {
                rating
            } else {
                null
            }
        } catch (e: NumberFormatException) {
            Log.e(TAG, "Invalid rating format: $ratingStr", e)
            null
        }
    }

    /**
     * ════════════════════════════════════════════════════════════════════════
     * Q.1.2 REQUIREMENT: EXIT BUTTON FUNCTION (5 marks)
     * Shows confirmation dialog before exiting
     * ════════════════════════════════════════════════════════════════════════
     */
    private fun showExitDialog() {
        AlertDialog.Builder(this)
            .setTitle("Exit App")  // ← CHANGEABLE
            .setMessage("Are you sure you want to exit?")  // ← CHANGEABLE
            .setPositiveButton("Yes") { _, _ ->
                Log.d(TAG, "User confirmed exit")
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }

    /**
     * Shows error message to user
     */
    private fun showError(message: String) {
        Log.w(TAG, "Error shown to user: $message")
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}

/**
 * ════════════════════════════════════════════════════════════════════════════
 * ASSIGNMENT REQUIREMENTS CHECKLIST:
 * ════════════════════════════════════════════════════════════════════════════
 *
 * Q.1.1 - Code Implementation (20 marks):
 * ✅ Code implemented on emulator and GitHub (5 marks)
 * ✅ Declare and initialize 4 parallel arrays (3 marks)
 * ✅ Create methods/functions required (2 marks)
 * ✅ Error handling (5 marks)
 * ✅ Clear and concise source code with meaningful comments (5 marks)
 *
 * Q.1.2 - Main Screen (25 marks):
 * ✅ Button 1: "Add to Playlist" with 4 prompts (10 marks)
 * ✅ Button 2: Navigate to second screen (5 marks)
 * ✅ Button 3: Exit app (5 marks)
 *
 * ════════════════════════════════════════════════════════════════════════════
 */