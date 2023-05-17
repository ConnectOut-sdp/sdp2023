package com.sdpteam.connectout.utils;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A test implementation of the MenuItem interface for testing return buttons in the ActionBar
 */
public class TestMenuItem implements MenuItem {

    private final int itemId;

    public TestMenuItem(int itemId) {
        this.itemId = itemId;
    }

    @Override
    public int getItemId() {
        return itemId;
    }

    @Override
    public int getGroupId() {
        return 0;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @NonNull
    @Override
    public MenuItem setTitle(@Nullable CharSequence title) {
        return null;
    }

    @NonNull
    @Override
    public MenuItem setTitle(int title) {
        return null;
    }

    @Nullable
    @Override
    public CharSequence getTitle() {
        return null;
    }

    @NonNull
    @Override
    public MenuItem setTitleCondensed(@Nullable CharSequence title) {
        return null;
    }

    @Nullable
    @Override
    public CharSequence getTitleCondensed() {
        return null;
    }

    @NonNull
    @Override
    public MenuItem setIcon(@Nullable Drawable icon) {
        return null;
    }

    @NonNull
    @Override
    public MenuItem setIcon(int iconRes) {
        return null;
    }

    @Nullable
    @Override
    public Drawable getIcon() {
        return null;
    }

    @NonNull
    @Override
    public MenuItem setIntent(@Nullable Intent intent) {
        return null;
    }

    @Nullable
    @Override
    public Intent getIntent() {
        return null;
    }

    @NonNull
    @Override
    public MenuItem setShortcut(char numericChar, char alphaChar) {
        return null;
    }

    @NonNull
    @Override
    public MenuItem setNumericShortcut(char numericChar) {
        return null;
    }

    @Override
    public char getNumericShortcut() {
        return 0;
    }

    @NonNull
    @Override
    public MenuItem setAlphabeticShortcut(char alphaChar) {
        return null;
    }

    @Override
    public char getAlphabeticShortcut() {
        return 0;
    }

    @NonNull
    @Override
    public MenuItem setCheckable(boolean checkable) {
        return null;
    }

    @Override
    public boolean isCheckable() {
        return false;
    }

    @NonNull
    @Override
    public MenuItem setChecked(boolean checked) {
        return null;
    }

    @Override
    public boolean isChecked() {
        return false;
    }

    @NonNull
    @Override
    public MenuItem setVisible(boolean visible) {
        return null;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @NonNull
    @Override
    public MenuItem setEnabled(boolean enabled) {
        return null;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public boolean hasSubMenu() {
        return false;
    }

    @Nullable
    @Override
    public SubMenu getSubMenu() {
        return null;
    }

    @NonNull
    @Override
    public MenuItem setOnMenuItemClickListener(@Nullable OnMenuItemClickListener menuItemClickListener) {
        return null;
    }

    @Nullable
    @Override
    public ContextMenu.ContextMenuInfo getMenuInfo() {
        return null;
    }

    @Override
    public void setShowAsAction(int actionEnum) {

    }

    @NonNull
    @Override
    public MenuItem setShowAsActionFlags(int actionEnum) {
        return null;
    }

    @NonNull
    @Override
    public MenuItem setActionView(@Nullable View view) {
        return null;
    }

    @NonNull
    @Override
    public MenuItem setActionView(int resId) {
        return null;
    }

    @Nullable
    @Override
    public View getActionView() {
        return null;
    }

    @NonNull
    @Override
    public MenuItem setActionProvider(@Nullable ActionProvider actionProvider) {
        return null;
    }

    @Nullable
    @Override
    public ActionProvider getActionProvider() {
        return null;
    }

    @Override
    public boolean expandActionView() {
        return false;
    }

    @Override
    public boolean collapseActionView() {
        return false;
    }

    @Override
    public boolean isActionViewExpanded() {
        return false;
    }

    @NonNull
    @Override
    public MenuItem setOnActionExpandListener(@Nullable OnActionExpandListener listener) {
        return null;
    }
}
