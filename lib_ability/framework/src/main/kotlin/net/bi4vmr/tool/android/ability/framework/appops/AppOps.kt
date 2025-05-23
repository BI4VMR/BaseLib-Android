package net.bi4vmr.tool.android.ability.framework.appops

/**
 * AppOp枚举。
 *
 * 本类的内容来自AOSP的"android/app/AppProtoEnums.java"。
 *
 * @since 1.0.0
 * @author bi4vmr@outlook.com
 */
enum class AppOps(

    /**
     * OP代码
     */
    val code: Int
) {

    NONE(-1),
    COARSE_LOCATION(0),
    FINE_LOCATION(1),
    GPS(2),
    VIBRATE(3),
    READ_CONTACTS(4),
    WRITE_CONTACTS(5),
    READ_CALL_LOG(6),
    WRITE_CALL_LOG(7),
    READ_CALENDAR(8),
    WRITE_CALENDAR(9),
    WIFI_SCAN(10),
    POST_NOTIFICATION(11),
    NEIGHBORING_CELLS(12),
    CALL_PHONE(13),
    READ_SMS(14),
    WRITE_SMS(15),
    RECEIVE_SMS(16),
    RECEIVE_EMERGENCY_SMS(17),
    RECEIVE_MMS(18),
    RECEIVE_WAP_PUSH(19),
    SEND_SMS(20),
    READ_ICC_SMS(21),
    WRITE_ICC_SMS(22),
    WRITE_SETTINGS(23),
    SYSTEM_ALERT_WINDOW(24),
    ACCESS_NOTIFICATIONS(25),
    CAMERA(26),
    RECORD_AUDIO(27),
    PLAY_AUDIO(28),
    READ_CLIPBOARD(29),
    WRITE_CLIPBOARD(30),
    TAKE_MEDIA_BUTTONS(31),
    TAKE_AUDIO_FOCUS(32),
    AUDIO_MASTER_VOLUME(33),
    AUDIO_VOICE_VOLUME(34),
    AUDIO_RING_VOLUME(35),
    AUDIO_MEDIA_VOLUME(36),
    AUDIO_ALARM_VOLUME(37),
    AUDIO_NOTIFICATION_VOLUME(38),
    AUDIO_BLUETOOTH_VOLUME(39),
    WAKE_LOCK(40),
    MONITOR_LOCATION(41),
    MONITOR_HIGH_POWER_LOCATION(42),
    GET_USAGE_STATS(43),
    MUTE_MICROPHONE(44),
    TOAST_WINDOW(45),
    PROJECT_MEDIA(46),
    ACTIVATE_VPN(47),
    WRITE_WALLPAPER(48),
    ASSIST_STRUCTURE(49),
    ASSIST_SCREENSHOT(50),
    READ_PHONE_STATE(51),
    ADD_VOICEMAIL(52),
    USE_SIP(53),
    PROCESS_OUTGOING_CALLS(54),
    USE_FINGERPRINT(55),
    BODY_SENSORS(56),
    READ_CELL_BROADCASTS(57),
    MOCK_LOCATION(58),
    READ_EXTERNAL_STORAGE(59),
    WRITE_EXTERNAL_STORAGE(60),
    TURN_SCREEN_ON(61),
    GET_ACCOUNTS(62),
    RUN_IN_BACKGROUND(63),
    AUDIO_ACCESSIBILITY_VOLUME(64),
    READ_PHONE_NUMBERS(65),
    REQUEST_INSTALL_PACKAGES(66),
    PICTURE_IN_PICTURE(67),
    INSTANT_APP_START_FOREGROUND(68),
    ANSWER_PHONE_CALLS(69),
    RUN_ANY_IN_BACKGROUND(70),
    CHANGE_WIFI_STATE(71),
    REQUEST_DELETE_PACKAGES(72),
    BIND_ACCESSIBILITY_SERVICE(73),
    ACCEPT_HANDOVER(74),
    MANAGE_IPSEC_TUNNELS(75),
    START_FOREGROUND(76),
    BLUETOOTH_SCAN(77),
    USE_BIOMETRIC(78),
    ACTIVITY_RECOGNITION(79),
    SMS_FINANCIAL_TRANSACTIONS(80),
    READ_MEDIA_AUDIO(81),
    WRITE_MEDIA_AUDIO(82),
    READ_MEDIA_VIDEO(83),
    WRITE_MEDIA_VIDEO(84),
    READ_MEDIA_IMAGES(85),
    WRITE_MEDIA_IMAGES(86),
    LEGACY_STORAGE(87),
    ACCESS_ACCESSIBILITY(88),
    READ_DEVICE_IDENTIFIERS(89),
    ACCESS_MEDIA_LOCATION(90),
    QUERY_ALL_PACKAGES(91),
    MANAGE_EXTERNAL_STORAGE(92),
    INTERACT_ACROSS_PROFILES(93),
    ACTIVATE_PLATFORM_VPN(94),
    LOADER_USAGE_STATS(95),
    DEPRECATED_1(96),
    AUTO_REVOKE_PERMISSIONS_IF_UNUSED(97),
    AUTO_REVOKE_MANAGED_BY_INSTALLER(98),
    NO_ISOLATED_STORAGE(99),
    PHONE_CALL_MICROPHONE(100),
    PHONE_CALL_CAMERA(101),
    RECORD_AUDIO_HOTWORD(102),
    MANAGE_ONGOING_CALLS(103),
    MANAGE_CREDENTIALS(104),
    USE_ICC_AUTH_WITH_DEVICE_IDENTIFIER(105),
    RECORD_AUDIO_OUTPUT(106),
    SCHEDULE_EXACT_ALARM(107),
    FINE_LOCATION_SOURCE(108),
    COARSE_LOCATION_SOURCE(109),
    MANAGE_MEDIA(110),
    BLUETOOTH_CONNECT(111),
    UWB_RANGING(112),
    ACTIVITY_RECOGNITION_SOURCE(113),
    BLUETOOTH_ADVERTISE(114),
    RECORD_INCOMING_PHONE_AUDIO(115),
    NEARBY_WIFI_DEVICES(116),
    ESTABLISH_VPN_SERVICE(117),
    ESTABLISH_VPN_MANAGER(118),
    ACCESS_RESTRICTED_SETTINGS(119),
    RECEIVE_AMBIENT_TRIGGER_AUDIO(120),
    RECEIVE_EXPLICIT_USER_INTERACTION_AUDIO(121),
    RUN_USER_INITIATED_JOBS(122),
    READ_MEDIA_VISUAL_USER_SELECTED(123),
    SYSTEM_EXEMPT_FROM_SUSPENSION(124),
    SYSTEM_EXEMPT_FROM_DISMISSIBLE_NOTIFICATIONS(125),
    READ_WRITE_HEALTH_DATA(126),
    FOREGROUND_SERVICE_SPECIAL_USE(127),
    SYSTEM_EXEMPT_FROM_POWER_RESTRICTIONS(128),
    SYSTEM_EXEMPT_FROM_HIBERNATION(129),
    SYSTEM_EXEMPT_FROM_ACTIVITY_BG_START_RESTRICTION(130),
    CAPTURE_CONSENTLESS_BUGREPORT_ON_USERDEBUG_BUILD(131),
    BODY_SENSORS_WRIST_TEMPERATURE(132),
    USE_FULL_SCREEN_INTENT(133),
    CAMERA_SANDBOXED(134),
    RECORD_AUDIO_SANDBOXED(135),
    RECEIVE_SANDBOX_TRIGGER_AUDIO(136),
    RECEIVE_SANDBOXED_DETECTION_TRAINING_DATA(137),
    CREATE_ACCESSIBILITY_OVERLAY(138),
    MEDIA_ROUTING_CONTROL(139),
    ENABLE_MOBILE_DATA_BY_USER(140),
    RESERVED_FOR_TESTING(141),
    RAPID_CLEAR_NOTIFICATIONS_BY_LISTENER(142);

    companion object {

        /**
         * 根据编码查找对应的AppOp。
         *
         * @param[code] 枚举编码。
         * @return OP。
         */
        @JvmStatic
        fun valueOf(code: Int): AppOps? {
            entries.forEach {
                if (it.code == code) {
                    return it
                }
            }

            return null
        }
    }
}
