package sa.vrtx.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import sa.vrtx.example.BuildConfig
import sa.vrtx.public.Vrtx
import sa.vrtx.public.configuration.Environment
import sa.vrtx.public.configuration.Language
import sa.vrtx.public.configuration.Mode

private val vrtxEnvironment: Environment =
    Environment.entries.find { it.name.equals(BuildConfig.VRTX_ENVIRONMENT, ignoreCase = true) }
        ?: Environment.Sandbox

private val InterFontFamily = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
    Font(R.font.inter_bold, FontWeight.Bold),
)

private val IbmPlexSansArabicFontFamily = FontFamily(
    Font(R.font.ibm_plex_sans_arabic_regular, FontWeight.Normal),
    Font(R.font.ibm_plex_sans_arabic_medium, FontWeight.Medium),
    Font(R.font.ibm_plex_sans_arabic_semibold, FontWeight.SemiBold),
    Font(R.font.ibm_plex_sans_arabic_bold, FontWeight.Bold),
)

private val GeomFontFamily = FontFamily(
    Font(R.font.geom_regular, FontWeight.Normal),
    Font(R.font.geom_medium, FontWeight.Medium),
    Font(R.font.geom_semibold, FontWeight.SemiBold),
    Font(R.font.geom_bold, FontWeight.Bold),
)

private val JuraFontFamily = FontFamily(
    Font(R.font.jura_regular, FontWeight.Normal),
    Font(R.font.jura_medium, FontWeight.Medium),
    Font(R.font.jura_semibold, FontWeight.SemiBold),
    Font(R.font.jura_bold, FontWeight.Bold),
)

private val NotoSansFontFamily = FontFamily(
    Font(R.font.noto_sans_regular, FontWeight.Normal),
    Font(R.font.noto_sans_medium, FontWeight.Medium),
    Font(R.font.noto_sans_semibold, FontWeight.SemiBold),
    Font(R.font.noto_sans_bold, FontWeight.Bold),
)

private val NotoKufiArabicFontFamily = FontFamily(
    Font(R.font.noto_kufi_arabic_regular, FontWeight.Normal),
    Font(R.font.noto_kufi_arabic_medium, FontWeight.Medium),
    Font(R.font.noto_kufi_arabic_semibold, FontWeight.SemiBold),
    Font(R.font.noto_kufi_arabic_bold, FontWeight.Bold),
)

private val NotoNaskhArabicFontFamily = FontFamily(
    Font(R.font.noto_naskh_arabic_regular, FontWeight.Normal),
    Font(R.font.noto_naskh_arabic_medium, FontWeight.Medium),
    Font(R.font.noto_naskh_arabic_semibold, FontWeight.SemiBold),
    Font(R.font.noto_naskh_arabic_bold, FontWeight.Bold),
)

private data class FontOption(val label: String, val fontFamily: FontFamily)

private val LatinFontOptions = listOf(
    FontOption("Inter", InterFontFamily),
    FontOption("Geom", GeomFontFamily),
    FontOption("Jura", JuraFontFamily),
    FontOption("Noto Sans", NotoSansFontFamily),
    FontOption("Jeju Gothic", FontFamily(Font(R.font.jejugothic_regular))),
    FontOption("Jockey One", FontFamily(Font(R.font.jockey_one_regular))),
)

private val ArabicFontOptions = listOf(
    FontOption("IBM Plex Sans Arabic", IbmPlexSansArabicFontFamily),
    FontOption("Noto Kufi Arabic", NotoKufiArabicFontFamily),
    FontOption("Noto Naskh Arabic", NotoNaskhArabicFontFamily),
)

private val Midnight = Color(0xFF07111F)
private val DeepNavy = Color(0xFF101F38)
private val Ink = Color(0xFF12233D)
private val Sky = Color(0xFF5CA9FF)
private val ElectricBlue = Color(0xFF377DFF)
private val Aqua = Color(0xFF4DE3D1)
private val Cloud = Color(0xFFF4F8FF)
private val Steel = Color(0xFF60708A)

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WelcomeScreen()
        }
    }
}

@Composable
private fun WelcomeScreen() {
    val context = LocalContext.current
    var language by remember { mutableStateOf(Language.English) }
    var mode by remember { mutableStateOf(Mode.LIGHT) }
    var selectedFontName by remember { mutableStateOf(LatinFontOptions.first().label) }
    var fontMenuExpanded by remember { mutableStateOf(false) }
    var isLaunching by remember { mutableStateOf(false) }
    var externalReference by remember { mutableStateOf("") }
    val isArabic = language == Language.Arabic
    val isDark = mode == Mode.DARK
    val colorScheme = if (isDark) {
        darkColorScheme(
            primary = Sky,
            onPrimary = Midnight,
            background = Midnight,
            onBackground = Cloud,
            surface = Color.White.copy(alpha = 0.09f),
            surfaceVariant = Color.White.copy(alpha = 0.06f),
            onSurface = Cloud,
        )
    } else {
        lightColorScheme(
            primary = ElectricBlue,
            onPrimary = Color.White,
            background = Cloud,
            onBackground = Ink,
            surface = Color.White.copy(alpha = 0.74f),
            surfaceVariant = Color.White.copy(alpha = 0.54f),
            onSurface = Ink,
        )
    }
    val muted = if (isDark) Color(0xFFB5C4DB) else Steel
    val glassBorder = if (isDark) Color.White.copy(alpha = 0.17f) else Color.White.copy(alpha = 0.82f)
    val pageBrush = if (isDark) {
        Brush.verticalGradient(listOf(Midnight, DeepNavy, Color(0xFF142B4A)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFFEAF3FF), Color(0xFFF7FAFF), Color(0xFFE7F5F6)))
    }
    val fontOptions = if (isArabic) ArabicFontOptions else LatinFontOptions
    val appFont = fontOptions.firstOrNull { it.label == selectedFontName }?.fontFamily
        ?: fontOptions.first().fontFamily

    MaterialTheme(colorScheme = colorScheme) {
        CompositionLocalProvider(LocalLayoutDirection provides if (isArabic) LayoutDirection.Rtl else LayoutDirection.Ltr) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(pageBrush),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Surface(
                        modifier = Modifier.size(84.dp),
                        shape = RoundedCornerShape(28.dp),
                        color = colorScheme.surface,
                        border = BorderStroke(1.dp, glassBorder),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("A", fontFamily = appFont, fontSize = 42.sp, fontWeight = FontWeight.Bold, color = Aqua)
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = if (isArabic) "أهلاً بك في" else "Welcome to",
                        fontFamily = appFont,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = muted,
                    )
                    Text(
                        text = "Atlas Pay",
                        fontFamily = appFont,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isArabic) "خدمات مصرفية متكاملة، صُممت بثقة." else "Business banking, designed with confidence.",
                        fontFamily = appFont,
                        fontSize = 15.sp,
                        color = muted,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(28.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(28.dp),
                        color = colorScheme.surface,
                        border = BorderStroke(1.dp, glassBorder),
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (isArabic) "تفضيلات التجربة" else "Experience preferences",
                                fontFamily = appFont,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = muted,
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            SettingToggle(
                label = if (isArabic) "اللغة العربية" else "English language",
                checked = isArabic,
                fontFamily = appFont,
                checkedLabel = "العربية",
                uncheckedLabel = "English",
                borderColor = glassBorder,
                onCheckedChange = {
                    language = if (language == Language.English) Language.Arabic else Language.English
                    selectedFontName = if (language == Language.Arabic) {
                        ArabicFontOptions.first().label
                    } else {
                        LatinFontOptions.first().label
                    }
                },
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            SettingToggle(
                label = if (isArabic) "المظهر" else "Appearance",
                checked = isDark,
                fontFamily = appFont,
                checkedLabel = if (isArabic) "داكن" else "Dark",
                uncheckedLabel = if (isArabic) "فاتح" else "Light",
                borderColor = glassBorder,
                onCheckedChange = {
                    mode = if (mode == Mode.LIGHT) Mode.DARK else Mode.LIGHT
                },
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            FontPicker(
                selectedFontName = selectedFontName,
                options = fontOptions,
                expanded = fontMenuExpanded,
                label = if (isArabic) "الخط" else "Font",
                borderColor = glassBorder,
                onExpandedChange = { fontMenuExpanded = it },
                onFontSelected = { selectedFontName = it },
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                value = externalReference,
                onValueChange = { externalReference = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(if (isArabic) "مرجع خارجي (اختياري)" else "External reference (optional)")
                },
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Aqua,
                    unfocusedBorderColor = glassBorder,
                    focusedLabelColor = Aqua,
                    unfocusedLabelColor = muted,
                    focusedTextColor = colorScheme.onSurface,
                    unfocusedTextColor = colorScheme.onSurface,
                    focusedContainerColor = colorScheme.surface,
                    unfocusedContainerColor = colorScheme.surface,
                ),
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.primary,
                    contentColor = Color.White,
                ),
                enabled = !isLaunching,
                onClick = {
                    isLaunching = true
                    Vrtx.setup(
                        clientId = BuildConfig.VRTX_CLIENT_ID,
                        clientSecret = BuildConfig.VRTX_CLIENT_SECRET,
                        environment = vrtxEnvironment,
                        language = language,
                        mode = mode,
                        fontFamily = appFont,
                        externalReference = externalReference,
                        onSuccess = {
                            isLaunching = false
                        },
                        onError = { err ->
                            isLaunching = false
                            Toast
                                .makeText(context, "Setup failed: ${err.message}", Toast.LENGTH_LONG)
                                .show()
                        },
                    )
                },
                    ) {
                if (isLaunching) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text(if (isArabic) "ابدأ الآن" else "Get started", fontFamily = appFont, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isArabic) "بيئة آمنة · وضع الحماية مفعل" else "Secure environment · Protection enabled",
                        fontFamily = appFont,
                        fontSize = 12.sp,
                        color = muted,
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingToggle(
    label: String,
    checked: Boolean,
    checkedLabel: String,
    uncheckedLabel: String,
    fontFamily: FontFamily,
    borderColor: Color,
    onCheckedChange: () -> Unit,
) {
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        onClick = onCheckedChange,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(label, fontFamily = fontFamily, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text(
                    text = if (checked) checkedLabel else uncheckedLabel,
                    fontFamily = fontFamily,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = { onCheckedChange() },
                colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.primary),
            )
        }
    }
}

@Composable
private fun FontPicker(
    selectedFontName: String,
    options: List<FontOption>,
    expanded: Boolean,
    label: String,
    borderColor: Color,
    onExpandedChange: (Boolean) -> Unit,
    onFontSelected: (String) -> Unit,
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, borderColor),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
            onClick = { onExpandedChange(true) },
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text("$selectedFontName  ▾", fontSize = 14.sp)
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(option.label, fontFamily = option.fontFamily)
                            if (option.label == selectedFontName) Text("✓", color = MaterialTheme.colorScheme.primary)
                        }
                    },
                    onClick = {
                        onFontSelected(option.label)
                        onExpandedChange(false)
                    },
                )
            }
        }
    }
}
