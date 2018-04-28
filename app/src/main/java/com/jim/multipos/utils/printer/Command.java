package com.jim.multipos.utils.printer;

public class Command {

	//Инициализация принтера
	public static byte[] ESC_Init = new byte[] {0x1b, 0x40 };
	
	/**
	 * Команда печати
	 */
	//Печать и упаковка
	public static byte[] LF = new byte[] {0x0a};
	
	//Печатная и подающая бумага
	public static byte[] ESC_J = new byte[] {0x1b, 0x4a, 0x00 };
	public static byte[] ESC_d = new byte[] {0x1b, 0x64, 0x00 };
	
	//Распечатать страницу самотестирования
	public static byte[] US_vt_eot = new byte[] {0x1f, 0x11, 0x04 };
	
	 //Звуковой сигнал
    public static byte[] ESC_B_m_n = new byte[] {0x1b, 0x42, 0x00, 0x00 };
	
    //Нарезка ножа
    public static byte[] GS_V_n = new byte[] {0x1d, 0x56, 0x00 };
    public static byte[] GS_V_m_n = new byte[] {0x1d, 0x56, 0x42, 0x00 };
    public static byte[] GS_i = new byte[] {0x1b, 0x69 };
    public static byte[] GS_m = new byte[] {0x1b, 0x6d };
	
	/**
	 * Команда настройки символов
	 */
	//Установите правый край символа
	public static byte[] ESC_SP = new byte[] {0x1b, 0x20, 0x00 };
	
	//Установка формата шрифта для печати символов
	public static byte[] ESC_ExclamationMark = new byte[] {0x1b, 0x21, 0x00 };
	
	//Установить ширину двойной ширины шрифта
	public static byte[] GS_ExclamationMark = new byte[] {0x1d, 0x21, 0x00 };
	
	//Установите обратную печать
	public static byte[] GS_B = new byte[] {0x1d, 0x42, 0x00 };
	
	//Отмена / выбор печати на 90 градусов
	public static byte[] ESC_V = new byte[] {0x1b, 0x56, 0x00 };
	
	//Выберите шрифты шрифта (главным образом ASCII)
	public static byte[] ESC_M = new byte[] {0x1b, 0x4d, 0x00 };
	
	//Выбрать / отменить выделение жирным шрифтом
	public static byte[] ESC_G = new byte[] {0x1b, 0x47, 0x00 };
	public static byte[] ESC_E = new byte[] {0x1b, 0x45, 0x00 };
	
	//Выбор / Отмена режима инвертированной печати
	public static byte[] ESC_LeftBrace = new byte[] {0x1b, 0x7b, 0x00 };
	
	//Установите высоту подчеркивания (символ)
	public static byte[] ESC_Minus = new byte[] {0x1b, 0x2d, 0x00 };
	
	//Режим символов
	public static byte[] FS_dot = new byte[] {0x1c, 0x2e };
	
	//Режим китайского символа
	public static byte[] FS_and = new byte[] {0x1c, 0x26 };
	
	//Задайте режим печати китайского символа
	public static byte[] FS_ExclamationMark = new byte[] {0x1c, 0x21, 0x00 };
	
	//Установите высоту подчеркивания (китайский символ)
	public static byte[] FS_Minus = new byte[] {0x1c, 0x2d, 0x00 };
	
	//Установите текст влево и вправо.
	public static byte[] FS_S = new byte[] {0x1c, 0x53, 0x00, 0x00 };
	
	//Выберите страницу кодов символов
	public static byte[] ESC_t = new byte[] {0x1b, 0x74, 0x00 };
	
	/**
	 * Инструкции по форматированию
	 */
	//Задайте интервал между строками по умолчанию
	public static byte[] ESC_Two = new byte[] {0x1b, 0x32}; 
	
	//Установка межстрочного интервала
	public static byte[] ESC_Three = new byte[] {0x1b, 0x33, 0x00 };
	
	//Установите режим выравнивания
	public static byte[] ESC_Align = new byte[] {0x1b, 0x61, 0x00 };
	
	//Установите левое поле
	public static byte[] GS_LeftSp = new byte[] {0x1d, 0x4c, 0x00 , 0x00 };
	
	//Установить абсолютную позицию печати
	//Установите текущую позицию в начало строки (nL + nH x 256).
	//Если позиция настройки находится за пределами указанной области печати, эта команда игнорируется
	public static byte[] ESC_Relative = new byte[] {0x1b, 0x24, 0x00, 0x00 };
	
	//Установите относительную позицию печати
	public static byte[] ESC_Absolute = new byte[] {0x1b, 0x5c, 0x00, 0x00 };
	
	//Установите ширину области печати
	public static byte[] GS_W = new byte[] {0x1d, 0x57, 0x00, 0x00 };

	/**
	 * Инструкция по статусу
	 */
	//Инструкции по передаче статуса в реальном времени
	public static byte[] DLE_eot = new byte[] {0x10, 0x04, 0x00 };
	
	//Инструкция в режиме реального времени в режиме реального времени
	public static byte[] DLE_DC4 = new byte[] {0x10, 0x14, 0x00, 0x00, 0x00 };
	
	//Стандартная инструкция на ящике
	public static byte[] ESC_p = new byte[] {0x1b, 0x70, 0x00, 0x00, 0x00 };
	
	/**
	 * Инструкции по настройке штрих-кода
	 */
	//Выберите метод печати HRI
	public static byte[] GS_H = new byte[] {0x1d, 0x48, 0x00 };
	
	//Установите высоту штрихового кода
	public static byte[] GS_h = new byte[] {0x1d, 0x68, (byte) 0xa2 };
	
	//Задайте ширину штрихового кода
	public static byte[] GS_w = new byte[] {0x1d, 0x77, 0x00 };
	
	//Установить шрифт шрифта HRI
	public static byte[] GS_f = new byte[] {0x1d, 0x66, 0x00 };
	
	//Инструкция по левому смещению штрих-кода
	public static byte[] GS_x = new byte[] {0x1d, 0x78, 0x00 };
	
	//Инструкции по печати штрих-кода
	public static byte[] GS_k = new byte[] {0x1d, 0x6b, 0x41, 0x0c };

	//Инструкции по QR-коду
    public static byte[] GS_k_m_v_r_nL_nH = new byte[] { 0x1b, 0x5a, 0x03, 0x03, 0x08, 0x00, 0x00 };
	
}
