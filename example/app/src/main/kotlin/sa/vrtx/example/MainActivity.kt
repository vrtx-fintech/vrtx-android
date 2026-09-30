package sa.vrtx.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.mandatorySystemGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import sa.vrtx.public.Vrtx
import sa.vrtx.public.configuration.DesignOption
import sa.vrtx.public.configuration.Environment
import sa.vrtx.public.configuration.Language
import sa.vrtx.public.configuration.Mode
import sa.vrtx.public.configuration.theme.ThemeOptions
import sa.vrtx.public.configuration.theme.VrtxColors
import sa.vrtx.public.configuration.theme.VrtxRadius
import sa.vrtx.public.configuration.theme.VrtxSpacing

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
private val ExampleThemeOptions = ThemeOptions(
    cardImage = "https://placehold.co/640x400/png".toUri(),
    brandLogo = "https://placehold.co/160x64/png".toUri(),
    brandName = "Atlas Pay",
    colors = VrtxColors(
        allBrands = VrtxColors.AllBrands(
            primary = ElectricBlue,
            buttonLabel = Color.White,
        ),
        labels = VrtxColors.Labels(
            primary = Ink,
            secondary = Steel,
            tertiary = Color(0xFF8B9AB2),
            quaternary = Color(0xFFB8C4D6),
        ),
        fills = VrtxColors.Fills(
            primary = Color(0xFFEAF3FF),
            secondary = Color(0xFFDCEAFF),
            tertiary = Color(0xFFC5D9F5),
            quaternary = Color(0xFFADC8EC),
            vibrant = VrtxColors.Fills.Vibrant(
                secondary = Aqua,
            ),
        ),
        backgrounds = VrtxColors.Backgrounds(
            primary = Cloud,
            secondary = Color(0xFFF7FAFF),
        ),
        backgroundsGradient = VrtxColors.BackgroundsGradients(
            wb01 = Color(0xFFEAF3FF),
            wb02 = Color(0xFFE7F5F6),
        ),
        accents = VrtxColors.Accents(
            red = Color(0xFFE05252),
            green = Color(0xFF2E9B67),
            greenBg = Color(0xFFE1F5EA),
        ),
    ),
    spacing = VrtxSpacing(
        sm = 8.dp,
        md = 12.dp,
        ml = 16.dp,
        lg = 20.dp,
    ),
    radius = VrtxRadius(
        s = 6.dp,
        sm = 8.dp,
        md = 12.dp,
        lg = 20.dp,
        full = 999.dp,
        huge = 64.dp,
    ),
)

/** Host-app state shared by the home and settings screens, so toggles survive navigation. */
private class PayState {
    var language by mutableStateOf(Language.English)
    var mode by mutableStateOf(Mode.LIGHT)
    var fontName by mutableStateOf(LatinFontOptions.first().label)
    var fontMenuOpen by mutableStateOf(false)
    var externalReference by mutableStateOf("")
    var launching by mutableStateOf(false)
    var showSettings by mutableStateOf(false)
    val isArabic: Boolean get() = language==Language.Arabic
    val isDark: Boolean get() = mode==Mode.DARK
    val fontOptions: List<FontOption> get() = if (isArabic) ArabicFontOptions else LatinFontOptions
    val fontFamily: FontFamily
        get() = fontOptions.firstOrNull { it.label==fontName }?.fontFamily
            ?: fontOptions.first().fontFamily

    fun toggleLanguage() {
        language = if (isArabic) Language.English else Language.Arabic
        fontName = fontOptions.first().label
    }
}

private fun tr(isArabic: Boolean, en: String, ar: String) = if (isArabic) ar else en

private class QuickAction(val label: String, val labelAr: String, val icon: ImageVector)

private val QuickActions = listOf(
    QuickAction("Transfer", "تحويل", Icons.Filled.SwapHoriz),
    QuickAction("Top up", "شحن", Icons.Filled.Add),
    QuickAction("Statements", "كشوف", Icons.AutoMirrored.Filled.ReceiptLong),
    QuickAction("Cards", "البطاقات", Icons.Filled.AccountBalance),
)

private class Txn(
    val label: String,
    val labelAr: String,
    val meta: String,
    val metaAr: String,
    val amount: String,
    val credit: Boolean,
)

private val Transactions = listOf(
    Txn("Payroll deposit", "إيداع رواتب", "Atlas Ops Ltd", "أطلس أوبش", "+$8,200.00", true),
    Txn(
        "Northwind Trading",
        "نورث ويند للتجارة",
        "Invoice #INV-20418",
        "فاتورة INV-20418",
        "+$4,250.00",
        true
    ),
    Txn("Contoso Cloud", "كونتوسو كلاود", "Subscription · Mar", "اشتراك · مارس", "-$89.00", false),
    Txn("Acme Supplies", "أكم للإمدادات", "Card ·· 4471", "بطاقة ·· 4471", "-$1,240.60", false),
)

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AtlasPayApp()
        }
    }
}

@Composable
private fun AtlasPayApp() {
    val pay = remember { PayState() }
    val colorScheme = if (pay.isDark) {
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
    val muted = if (pay.isDark) Color(0xFFB5C4DB) else Steel
    val glassBorder =
        if (pay.isDark) Color.White.copy(alpha = 0.17f) else Color.White.copy(alpha = 0.82f)
    val pageBrush = if (pay.isDark) {
        Brush.verticalGradient(listOf(Midnight, DeepNavy, Color(0xFF142B4A)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFFEAF3FF), Color(0xFFF7FAFF), Color(0xFFE7F5F6)))
    }

    MaterialTheme(colorScheme = colorScheme) {
        CompositionLocalProvider(
            LocalLayoutDirection provides if (pay.isArabic) LayoutDirection.Rtl else LayoutDirection.Ltr,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(pageBrush),
            ) {
                if (pay.showSettings) {
                    BackHandler { pay.showSettings = false }
                    SettingsScreen(pay, muted, glassBorder)
                } else {
                    HomeScreen(pay, muted, glassBorder)
                }
            }
        }
    }
}

@Composable
private fun HomeScreen(
    pay: PayState,
    muted: Color,
    glassBorder: Color,
) {
    val context = LocalContext.current
    val scheme = MaterialTheme.colorScheme
    val font = pay.fontFamily
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing.union(WindowInsets.mandatorySystemGestures))
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
    ) {
        HomeTopBar(pay, font, muted)
        Spacer(modifier = Modifier.height(20.dp))
        BalanceCard(pay, font)
        Spacer(modifier = Modifier.height(22.dp))
        QuickActionRow(pay, font, muted, glassBorder)
        Spacer(modifier = Modifier.height(24.dp))
        SectionHeader(
            title = tr(pay.isArabic, "Recent transactions", "المعاملات الأخيرة"),
            action = tr(pay.isArabic, "See all", "عرض الكل"),
            font = font,
            muted = muted,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = scheme.surface,
            border = BorderStroke(1.dp, glassBorder),
        ) {
            Column {
                Transactions.forEachIndexed { index, txn ->
                    TransactionRow(pay, txn, font, muted)
                    if (index!=Transactions.lastIndex) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 64.dp)
                                .height(1.dp)
                                .background(glassBorder),
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = scheme.primary,
                contentColor = Color.White,
            ),
            enabled = !pay.launching,
            onClick = {
                pay.launching = true
                Vrtx.setup(
                    clientId = BuildConfig.VRTX_CLIENT_ID,
                    clientSecret = BuildConfig.VRTX_CLIENT_SECRET,
                    environment = vrtxEnvironment,
                    language = pay.language,
                    designOption = DesignOption.OptionC,
                    mode = pay.mode,
                    theme = ExampleThemeOptions,
                    fontFamily = font,
                    externalReference = pay.externalReference,
                    onSuccess = {
                        pay.launching = false
                    },
                    onError = { err ->
                        pay.launching = false
                        Toast
                            .makeText(context, "Setup failed: ${err.message}", Toast.LENGTH_LONG)
                            .show()
                    },
                    onExit = {
                        pay.launching = false
                    },
                )
            },
        ) {
            if (pay.launching) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp,
                )
            } else {
                Text(
                    text = tr(pay.isArabic, "Get started", "ابدأ الآن"),
                    fontFamily = font,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                )
            }
        }
        Spacer(modifier = Modifier.height(14.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.Shield,
                contentDescription = null,
                tint = muted,
                modifier = Modifier.size(14.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = tr(
                    pay.isArabic,
                    "Secure environment · Protection enabled",
                    "بيئة آمنة · وضع الحماية مفعل"
                ),
                fontFamily = font,
                fontSize = 12.sp,
                color = muted,
            )
        }
    }
}

@Composable
private fun HomeTopBar(
    pay: PayState,
    font: FontFamily,
    muted: Color,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            modifier = Modifier.size(44.dp),
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.primary,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    "A",
                    fontFamily = font,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = "Atlas Pay",
                fontFamily = font,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = tr(pay.isArabic, "Business account", "حساب الأعمال"),
                fontFamily = font,
                fontSize = 12.sp,
                color = muted,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { pay.showSettings = true }) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = tr(pay.isArabic, "Settings", "الإعدادات"),
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Composable
private fun BalanceCard(
    pay: PayState,
    font: FontFamily,
) {
    val cardBrush = if (pay.isDark) {
        Brush.linearGradient(listOf(Color(0xFF1B3A6B), Color(0xFF14304F)))
    } else {
        Brush.linearGradient(listOf(ElectricBlue, Color(0xFF2F6BE0)))
    }
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        color = Color.Transparent,
    ) {
        Column(
            modifier = Modifier
                .background(cardBrush)
                .padding(20.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = tr(pay.isArabic, "Total balance", "إجمالي الرصيد"),
                    fontFamily = font,
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.82f),
                )
                Spacer(modifier = Modifier.weight(1f))
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = Color.White.copy(alpha = 0.18f),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = Aqua,
                            modifier = Modifier.size(13.dp),
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = tr(pay.isArabic, "Verified", "موثّق"),
                            fontFamily = font,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "$24,680.50",
                fontFamily = font,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
            Text(
                text = "USD · •••• 4471",
                fontFamily = font,
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.78f),
            )
            Spacer(modifier = Modifier.height(18.dp))
            Row {
                BalanceStat(font, tr(pay.isArabic, "Income", "الدخل"), "+$8,200", credit = true)
                Spacer(modifier = Modifier.width(28.dp))
                BalanceStat(font, tr(pay.isArabic, "Spent", "المصروفات"), "-$2,140", credit = false)
            }
        }
    }
}

@Composable
private fun BalanceStat(
    font: FontFamily,
    label: String,
    amount: String,
    credit: Boolean,
) {
    Column {
        Text(label, fontFamily = font, fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = amount,
            fontFamily = font,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (credit) Aqua else Color.White,
        )
    }
}

@Composable
private fun QuickActionRow(
    pay: PayState,
    font: FontFamily,
    muted: Color,
    glassBorder: Color,
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        QuickActions.forEach { action ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(74.dp),
            ) {
                Surface(
                    modifier = Modifier.size(52.dp),
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, glassBorder),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = action.icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp),
                        )
                    }
                }
                Spacer(modifier = Modifier.height(7.dp))
                Text(
                    text = tr(pay.isArabic, action.label, action.labelAr),
                    fontFamily = font,
                    fontSize = 11.sp,
                    color = muted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    action: String,
    font: FontFamily,
    muted: Color,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = title,
            fontFamily = font,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(text = action, fontFamily = font, fontSize = 12.sp, color = muted)
    }
}

@Composable
private fun TransactionRow(
    pay: PayState,
    txn: Txn,
    font: FontFamily,
    muted: Color,
) {
    val tint = if (txn.credit) Aqua else ElectricBlue
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            modifier = Modifier.size(38.dp),
            shape = RoundedCornerShape(13.dp),
            color = tint.copy(alpha = 0.16f),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = if (txn.credit) {
                        Icons.AutoMirrored.Filled.TrendingUp
                    } else {
                        Icons.AutoMirrored.Filled.TrendingDown
                    },
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = tr(pay.isArabic, txn.label, txn.labelAr),
                fontFamily = font,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = tr(pay.isArabic, txn.meta, txn.metaAr),
                fontFamily = font,
                fontSize = 12.sp,
                color = muted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = txn.amount,
            fontFamily = font,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (txn.credit) Aqua else MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun SettingsScreen(
    pay: PayState,
    muted: Color,
    glassBorder: Color,
) {
    val scheme = MaterialTheme.colorScheme
    val font = pay.fontFamily
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing.union(WindowInsets.mandatorySystemGestures))
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { pay.showSettings = false }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = tr(pay.isArabic, "Back", "رجوع"),
                    tint = scheme.onBackground,
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = tr(pay.isArabic, "Settings", "الإعدادات"),
                fontFamily = font,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = scheme.onBackground,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = tr(pay.isArabic, "Preferences", "التفضيلات"),
            fontFamily = font,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = muted,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = scheme.surface,
            border = BorderStroke(1.dp, glassBorder),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                SettingToggle(
                    label = tr(pay.isArabic, "English language", "اللغة العربية"),
                    checked = pay.isArabic,
                    fontFamily = font,
                    checkedLabel = "العربية",
                    uncheckedLabel = "English",
                    borderColor = glassBorder,
                    onCheckedChange = { pay.toggleLanguage() },
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingToggle(
                    label = tr(pay.isArabic, "Appearance", "المظهر"),
                    checked = pay.isDark,
                    fontFamily = font,
                    checkedLabel = tr(pay.isArabic, "Dark", "داكن"),
                    uncheckedLabel = tr(pay.isArabic, "Light", "فاتح"),
                    borderColor = glassBorder,
                    onCheckedChange = { pay.mode = if (pay.isDark) Mode.LIGHT else Mode.DARK },
                )
                Spacer(modifier = Modifier.height(8.dp))
                FontPicker(
                    selectedFontName = pay.fontName,
                    options = pay.fontOptions,
                    expanded = pay.fontMenuOpen,
                    label = tr(pay.isArabic, "Font", "الخط"),
                    borderColor = glassBorder,
                    onExpandedChange = { pay.fontMenuOpen = it },
                    onFontSelected = { pay.fontName = it },
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = tr(pay.isArabic, "Integration", "التكامل"),
            fontFamily = font,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = muted,
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = pay.externalReference,
            onValueChange = { pay.externalReference = it },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(
                    tr(
                        pay.isArabic,
                        "External reference (optional)",
                        "مرجع خارجي (اختياري)"
                    )
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(18.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Aqua,
                unfocusedBorderColor = glassBorder,
                focusedLabelColor = Aqua,
                unfocusedLabelColor = muted,
                focusedTextColor = scheme.onSurface,
                unfocusedTextColor = scheme.onSurface,
                focusedContainerColor = scheme.surface,
                unfocusedContainerColor = scheme.surface,
            ),
        )
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
            .fillMaxWidth(),
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
                Text(
                    label,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
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
                            if (option.label==selectedFontName) Text(
                                "✓",
                                color = MaterialTheme.colorScheme.primary
                            )
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
