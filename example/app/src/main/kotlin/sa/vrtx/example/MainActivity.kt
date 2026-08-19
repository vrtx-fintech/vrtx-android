package sa.vrtx.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import java.util.UUID
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

private val AtlasTeal = Color(0xFF0E5C56)
private val AtlasBackground = Color(0xFFF5F1EA)
private val AtlasOnSurface = Color(0xFF132724)
private val AtlasMuted = Color(0xFF6B7B78)
private val AtlasIllustration = Color(0xFFE8E4DC)
private val AtlasDarkBackground = Color(0xFF10211E)
private val AtlasDarkSurface = Color(0xFF193330)
private val AtlasDarkOnSurface = Color(0xFFE9F2EF)
private val AtlasDarkMuted = Color(0xFFB2C3BD)
private val AtlasDarkIllustration = Color(0xFF21443E)

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
    val isArabic = language == Language.Arabic
    val isDark = mode == Mode.DARK
    val colorScheme = if (isDark) {
        darkColorScheme(
            primary = Color(0xFF56B4A5),
            onPrimary = Color(0xFF062B26),
            background = AtlasDarkBackground,
            onBackground = AtlasDarkOnSurface,
            surface = AtlasDarkSurface,
            onSurface = AtlasDarkOnSurface,
        )
    } else {
        lightColorScheme(
            primary = AtlasTeal,
            onPrimary = Color.White,
            background = AtlasBackground,
            onBackground = AtlasOnSurface,
            surface = AtlasBackground,
            onSurface = AtlasOnSurface,
        )
    }
    val muted = if (isDark) AtlasDarkMuted else AtlasMuted
    val illustration = if (isDark) AtlasDarkIllustration else AtlasIllustration
    val fontOptions = if (isArabic) ArabicFontOptions else LatinFontOptions
    val appFont = fontOptions.firstOrNull { it.label == selectedFontName }?.fontFamily
        ?: fontOptions.first().fontFamily

    MaterialTheme(colorScheme = colorScheme) {
        CompositionLocalProvider(LocalLayoutDirection provides if (isArabic) LayoutDirection.Rtl else LayoutDirection.Ltr) {
            Surface(modifier = Modifier.fillMaxSize(), color = colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(0.32f))
            Surface(
                modifier = Modifier.size(220.dp),
                shape = RoundedCornerShape(28.dp),
                color = illustration,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("A", fontFamily = appFont, fontSize = 88.sp, fontWeight = FontWeight.Bold, color = colorScheme.primary)
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = if (isArabic) "مرحبًا بك في" else "Welcome to",
                fontFamily = appFont,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "Atlas Pay",
                fontFamily = appFont,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = if (isArabic) "خدمات مصرفية مصممة للأعمال الحديثة" else "Banking built for modern businesses",
                fontFamily = appFont,
                fontSize = 16.sp,
                color = muted,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.weight(0.25f))
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp, muted.copy(alpha = 0.45f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = colorScheme.onSurface),
                onClick = {
                    language = if (language == Language.English) Language.Arabic else Language.English
                    selectedFontName = if (language == Language.Arabic) {
                        ArabicFontOptions.first().label
                    } else {
                        LatinFontOptions.first().label
                    }
                },
            ) {
                Text(
                    text = if (language == Language.English) "English" else "العربية",
                    fontFamily = appFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = colorScheme.onSurface,
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp, muted.copy(alpha = 0.45f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = colorScheme.onSurface),
                onClick = {
                    mode = if (mode == Mode.LIGHT) Mode.DARK else Mode.LIGHT
                },
            ) {
                Text(
                    text = if (mode == Mode.LIGHT) "Light" else "Dark",
                    fontFamily = appFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = colorScheme.onSurface,
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    border = BorderStroke(1.dp, muted.copy(alpha = 0.45f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = colorScheme.onSurface),
                    onClick = { fontMenuExpanded = true },
                ) {
                    Text(
                        text = if (isArabic) "الخط: $selectedFontName" else "Font: $selectedFontName",
                        fontFamily = appFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                    )
                }
                DropdownMenu(
                    expanded = fontMenuExpanded,
                    onDismissRequest = { fontMenuExpanded = false },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    fontOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.label, fontFamily = option.fontFamily) },
                            onClick = {
                                selectedFontName = option.label
                                fontMenuExpanded = false
                            },
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AtlasTeal,
                    contentColor = Color.White,
                ),
                onClick = {
                    Vrtx.setup(
                        clientId = BuildConfig.VRTX_CLIENT_ID,
                        clientSecret = BuildConfig.VRTX_CLIENT_SECRET,
                        environment = vrtxEnvironment,
                        language = language,
                        mode = mode,
                        fontFamily = appFont,
                        externalReference = UUID.randomUUID().toString(),
                        onError = { err ->
                            Toast
                                .makeText(context, "Setup failed: ${err.message}", Toast.LENGTH_LONG)
                                .show()
                        },
                    )
                },
            ) {
                Text(if (isArabic) "ابدأ الآن" else "Get started", fontFamily = appFont, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
            }
        }
    }
}
