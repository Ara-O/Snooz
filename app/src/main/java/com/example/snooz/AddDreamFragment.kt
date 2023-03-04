package com.example.snooz

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aallam.openai.api.ExperimentalOpenAI
import com.aallam.openai.api.completion.CompletionRequest
import com.aallam.openai.api.completion.TextCompletion
import com.aallam.openai.api.image.ImageCreationURL
import com.aallam.openai.api.image.ImageSize
import com.aallam.openai.api.model.Model
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.example.snooz.databinding.FragmentAddDreamBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import java.util.*

//Without id, Dream class is with id tag cause getting id from firebase
class DreamData{
    var name = ""
    var tag = ""
    var text = ""
    var date = ""
    constructor(name: String, tag: String, text: String, date: String){
        this.name = name
        this.tag= tag
        this.text = text
        this.date = date
    }
}

class AddDreamFragment : Fragment() {
    private var _binding: FragmentAddDreamBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference

    @OptIn(ExperimentalOpenAI::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddDreamBinding.inflate(inflater, container, false)
        val view = binding.root

        val openAI = OpenAI(BuildConfig.API_KEY)

//        runBlocking {
//
//        val images = openAI.image( // or openAI.imageJSON
//            creation = ImageCreationURL(
//                prompt = "Make an image that represents this dream - I had a dream that I was floating in a giant bowl of soup. The soup was made of rainbows and unicorns, and every time I took a sip, I could hear their laughter. Suddenly, a giant spoon appeared and started chasing me around the bowl, trying to scoop me up. Just as the spoon was about to catch me, I sprouted wings and flew away, only to wake up with a strange craving for soup.",
//                n = 2,
//                size = ImageSize.is1024x1024
//            )
//        )
//            Log.d("eaa", images.toString())
//        }

        database = Firebase.database.reference

        //Getting the current date in DD/MM/YY format so I can add it to the firebase dreams call
        val dateTime = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val formattedDateTime = formatter.format(dateTime)

        //Get users current id
        val storedData = activity?.getSharedPreferences("sharedPreferences", AppCompatActivity.MODE_PRIVATE)
        val sharedPreferencesId = storedData?.getString("id", "")

        //once the user clicks the button, push the data to firebase
        _binding!!.addDreamButton.setOnClickListener {

            val currentDreamData = DreamData(binding.dreamNameInput.text.toString(), binding.dreamTagInput.text.toString(), binding.dreamTextInput.text.toString(), formattedDateTime);
            database.child("/${sharedPreferencesId}/dreams").push().setValue(currentDreamData)

            Toast.makeText(context, "Dream added successfully",
                Toast.LENGTH_SHORT).show()
            binding.dreamNameInput.text.clear()
            binding.dreamTagInput.text.clear()
            binding.dreamTextInput.text.clear()
        }

        _binding!!.autocompleteDreamButton.setOnClickListener{
            autocompleteDream(openAI, binding.dreamTextInput.text.toString())
        }
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun autocompleteDream(openAI: OpenAI, dream: String){
        runBlocking{
            val models: List<Model> = openAI.models()
            Log.d("models", models.toString())
            val completionRequest = CompletionRequest(
                model = ModelId("text-davinci-003"),
                prompt = "autocorrect this statement - ${dream}",
                echo = false
            )
            val completion: TextCompletion = openAI.completion(completionRequest)

            val autocorrectResult = completion.choices[0].text

            binding.dreamTextInput.setText(autocorrectResult.replace("\"", ""))
        }
    }
}