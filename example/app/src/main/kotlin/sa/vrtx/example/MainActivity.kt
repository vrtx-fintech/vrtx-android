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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.launch
import sa.vrtx.android.Vrtx
import sa.vrtx.example.BuildConfig
import sa.vrtx.sdk.SdkConfig
import sa.vrtx.sdk.domain.models.Environment

private val vrtxEnvironment: Environment =
    Environment.entries.find { it.name.equals(BuildConfig.VRTX_ENVIRONMENT, ignoreCase = true) }
        ?: Environment.Sandbox

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
                WelcomeScreen(activity = this)
            }
        }
    }
}

@Composable
private fun WelcomeScreen(activity: FragmentActivity) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var selectedTab by remember { mutableIntStateOf(2) }

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
                    scope.launch {
                        Vrtx.init(
                            activity = activity,
                            config = SdkConfig(
                                environment = vrtxEnvironment,
                                clientId = BuildConfig.VRTX_CLIENT_ID,
                                clientSecret = BuildConfig.VRTX_CLIENT_SECRET,
                            ),
                            onError = { err ->
                                Toast
                                    .makeText(context, "Init failed: ${err.message}", Toast.LENGTH_LONG)
                                    .show()
                            },
                        )
                    }
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
