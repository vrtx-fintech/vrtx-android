package sa.vrtx.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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

private val AtlasTeal = Color(0xFF0E5C56)
private val AtlasBackground = Color(0xFFF5F1EA)
private val AtlasOnSurface = Color(0xFF132724)
private val AtlasMuted = Color(0xFF6B7B78)
private val AtlasIllustration = Color(0xFFE8E4DC)

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = AtlasTeal,
                    onPrimary = Color.White,
                    background = AtlasBackground,
                    onBackground = AtlasOnSurface,
                    surface = AtlasBackground,
                    onSurface = AtlasOnSurface,
                ),
            ) {
                WelcomeScreen()
            }
        }
    }
}

@Composable
private fun WelcomeScreen() {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(2) }
    var language by remember { mutableStateOf(Language.English) }
    var mode by remember { mutableStateOf(Mode.LIGHT) }

    Scaffold(
        containerColor = AtlasBackground,
        bottomBar = {
            NavigationBar(containerColor = AtlasBackground) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Outlined.Home, contentDescription = null) },
                    label = { Text("Home") },
                    colors = atlasItemColors(),
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Outlined.History, contentDescription = null) },
                    label = { Text("Activity") },
                    colors = atlasItemColors(),
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(RoundedCornerShape(7.dp))
                                .background(AtlasTeal),
                        )
                    },
                    label = { Text("Atlas Pay", fontWeight = FontWeight.SemiBold) },
                    colors = atlasItemColors(),
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Outlined.CreditCard, contentDescription = null) },
                    label = { Text("Cards") },
                    colors = atlasItemColors(),
                )
                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    icon = { Icon(Icons.Outlined.Person, contentDescription = null) },
                    label = { Text("Profile") },
                    colors = atlasItemColors(),
                )
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(0.4f))
            Surface(
                modifier = Modifier.size(220.dp),
                shape = RoundedCornerShape(28.dp),
                color = AtlasIllustration,
                content = {},
            )
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "Welcome to",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = AtlasOnSurface,
            )
            Text(
                text = "Atlas Pay",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = AtlasTeal,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Banking built for modern businesses",
                fontSize = 16.sp,
                color = AtlasMuted,
            )
            Spacer(modifier = Modifier.weight(0.3f))
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                onClick = {
                    language = if (language == Language.English) Language.Arabic else Language.English
                },
            ) {
                Text(
                    text = if (language == Language.English) "English" else "العربية",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = AtlasOnSurface,
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                onClick = {
                    mode = if (mode == Mode.LIGHT) Mode.DARK else Mode.LIGHT
                },
            ) {
                Text(
                    text = if (mode == Mode.LIGHT) "Light" else "Dark",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = AtlasOnSurface,
                )
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
                        fontFamily = if (language == Language.English) InterFontFamily else IbmPlexSansArabicFontFamily,
                        externalReference = UUID.randomUUID().toString(),
                        onError = { err ->
                            Toast
                                .makeText(context, "Setup failed: ${err.message}", Toast.LENGTH_LONG)
                                .show()
                        },
                    )
                },
            ) {
                Text("Get started", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun atlasItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = AtlasTeal,
    selectedTextColor = AtlasTeal,
    indicatorColor = Color.Transparent,
    unselectedIconColor = AtlasMuted,
    unselectedTextColor = AtlasMuted,
)
