import android.app.DatePickerDialog
import android.content.Context
import java.util.*

object DatePickerUtil {

    fun showDatePicker(context: Context, dateSetListener: DatePickerDialog.OnDateSetListener) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(context, dateSetListener, year, month, day)
        datePickerDialog.show()
    }
}