/*
 * Copyright (c) 2017, marcin
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package resources.strings;

/**
 * This class holds string resources for use in program.
 * 
 * @author marcin
 */
public abstract class Strings {
    public final String errorDialogTitle, programTitle, menu_program_title, menu_help_title, menu_program_close_item,
            menu_help_about_program_item, about_program_message, range_choosing_tab1, range_choosing_tab2,
            range_choosing_tab3, range_choosing_index_address, range_choosing_address_start, range_choosing_address_end,
            range_choosing_number_of_chapters, range_choosing_confirm_button, dialog_invalid_address_message,
            dialog_io_error_message, dialog_chapter_selection_title, dialog_progress_title, dialog_file_create,
            book_creating_chapters_success, book_creating_chapters_failure, book_creating_chapters_total,
            book_creating_button_cancel, book_creating_button_back, dialog_no_chapter_selected_message,
            dialog_invalid_filename_message, book_creating_list_success_entry, book_creating_list_failure_entry,
            dialog_book_creating_report_title, dialog_book_creating_report_message,
            range_choosing_next_chapter_link_name, menu_program_settings_item, settings_note, settings_language,
            settings_format, settings_html_element, settings_website_encoding, settings_pdf_font, settings_title_at_the_end;

    public Strings(String errorDialogTitle, String programTitle, String menu_program_title, String menu_help_title,
            String menu_program_close_item, String menu_help_about_program_item, String about_program_message,
            String range_choosing_tab1, String range_choosing_tab2, String range_choosing_tab3,
            String range_choosing_index_address, String range_choosing_address_start, String range_choosing_address_end,
            String range_choosing_number_of_chapters, String range_choosing_confirm_button,
            String dialog_invalid_address_message, String dialog_io_error_message,
            String dialog_chapter_selection_title, String dialog_progress_title, String dialog_file_create,
            String book_creating_chapters_success, String book_creating_chapters_failure,
            String book_creating_chapters_total, String book_creating_button_cancel, String book_creating_button_back,
            String dialog_no_chapter_selected_message, String dialog_invalid_filename_message,
            String book_creating_list_success_entry, String book_creating_list_failure_entry,
            String dialog_book_creating_report_title, String dialog_book_creating_report_message,
            String range_choosing_next_chapter_link_name, String menu_program_settings_item, String settings_note,
            String settings_language, String settings_format, String settings_html_element,
            String settings_website_encoding, String settings_pdf_font, String settings_title_at_the_end) {
        this.errorDialogTitle = errorDialogTitle;
        this.programTitle = programTitle;
        this.menu_program_title = menu_program_title;
        this.menu_help_title = menu_help_title;
        this.menu_program_close_item = menu_program_close_item;
        this.menu_help_about_program_item = menu_help_about_program_item;
        this.about_program_message = about_program_message;
        this.range_choosing_tab1 = range_choosing_tab1;
        this.range_choosing_tab2 = range_choosing_tab2;
        this.range_choosing_tab3 = range_choosing_tab3;
        this.range_choosing_index_address = range_choosing_index_address;
        this.range_choosing_address_start = range_choosing_address_start;
        this.range_choosing_address_end = range_choosing_address_end;
        this.range_choosing_number_of_chapters = range_choosing_number_of_chapters;
        this.range_choosing_confirm_button = range_choosing_confirm_button;
        this.dialog_invalid_address_message = dialog_invalid_address_message;
        this.dialog_io_error_message = dialog_io_error_message;
        this.dialog_chapter_selection_title = dialog_chapter_selection_title;
        this.dialog_progress_title = dialog_progress_title;
        this.dialog_file_create = dialog_file_create;
        this.book_creating_chapters_success = book_creating_chapters_success;
        this.book_creating_chapters_failure = book_creating_chapters_failure;
        this.book_creating_chapters_total = book_creating_chapters_total;
        this.book_creating_button_cancel = book_creating_button_cancel;
        this.book_creating_button_back = book_creating_button_back;
        this.dialog_no_chapter_selected_message = dialog_no_chapter_selected_message;
        this.dialog_invalid_filename_message = dialog_invalid_filename_message;
        this.book_creating_list_success_entry = book_creating_list_success_entry;
        this.book_creating_list_failure_entry = book_creating_list_failure_entry;
        this.dialog_book_creating_report_title = dialog_book_creating_report_title;
        this.dialog_book_creating_report_message = dialog_book_creating_report_message;
        this.range_choosing_next_chapter_link_name = range_choosing_next_chapter_link_name;
        this.menu_program_settings_item = menu_program_settings_item;
        this.settings_note = settings_note;
        this.settings_language = settings_language;
        this.settings_format = settings_format;
        this.settings_html_element = settings_html_element;
        this.settings_website_encoding = settings_website_encoding;
        this.settings_pdf_font = settings_pdf_font;
        this.settings_title_at_the_end = settings_title_at_the_end;
    }

}
