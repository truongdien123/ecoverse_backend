-- Xóa dữ liệu cũ
DELETE FROM waste_items;
DELETE FROM waste_bins;

-- 4 LOẠI THÙNG RÁC
INSERT INTO waste_bins (id, code, display_name, color_hex, icon_url, description, order_index, active) VALUES
    ('wb001', 'ORGANIC',   'Rác hữu cơ',   '#22C55E', NULL, 'Thùng rác chứa các chất thải hữu cơ dễ phân hủy sinh học', 1, true),
    ('wb002', 'RECYCLE',   'Rác tái chế',   '#3B82F6', NULL, 'Thùng rác chứa các vật liệu có thể tái chế được',         2, true),
    ('wb003', 'INORGANIC', 'Rác vô cơ',     '#EAB308', NULL, 'Thùng rác chứa các chất thải vô cơ không tái chế được',   3, true),
    ('wb004', 'HAZARDOUS', 'Rác nguy hại',  '#EF4444', NULL, 'Thùng rác chứa các chất thải nguy hại cho sức khỏe',      4, true);

-- 20 WASTE ITEMS
INSERT INTO waste_items (id, name, correct_bin_code, description, image_url, created_by) VALUES
    ('wi001', 'Vỏ chuối',             'ORGANIC',   'Phế phẩm thực phẩm dễ phân hủy, có thể làm phân bón.', NULL, 'ADMIN'),
    ('wi002', 'Rau củ hư',            'ORGANIC',   'Rau củ quả bị hư hỏng, phân hủy tự nhiên.',            NULL, 'ADMIN'),
    ('wi003', 'Bã cà phê',            'ORGANIC',   'Bã cà phê sau khi pha, có thể ủ phân.',                NULL, 'ADMIN'),
    ('wi004', 'Cơm thừa',             'ORGANIC',   'Thức ăn thừa dễ phân hủy sinh học.',                   NULL, 'ADMIN'),
    ('wi005', 'Lá cây khô',           'ORGANIC',   'Lá cây rụng, cành cây nhỏ.',                           NULL, 'ADMIN'),
    ('wi006', 'Chai nhựa PET',         'RECYCLE',   'Các loại chai nước giải khát, vỏ hộp nhựa sạch.',      NULL, 'ADMIN'),
    ('wi007', 'Lon nước ngọt',         'RECYCLE',   'Lon nhôm đựng nước ngọt, bia.',                        NULL, 'ADMIN'),
    ('wi008', 'Giấy báo cũ',          'RECYCLE',   'Báo giấy, tạp chí đã đọc xong.',                       NULL, 'ADMIN'),
    ('wi009', 'Thùng carton',          'RECYCLE',   'Hộp carton đã qua sử dụng.',                           NULL, 'ADMIN'),
    ('wi010', 'Chai thủy tinh',        'RECYCLE',   'Chai lọ thủy tinh sạch.',                              NULL, 'ADMIN'),
    ('wi011', 'Hộp xốp',              'INORGANIC', 'Xốp, sành sứ, cao su không thể tái chế.',              NULL, 'ADMIN'),
    ('wi012', 'Túi nilon bẩn',         'INORGANIC', 'Túi nilon đã dính bẩn, không tái chế được.',           NULL, 'ADMIN'),
    ('wi013', 'Tã lót đã dùng',        'INORGANIC', 'Tã em bé, băng vệ sinh đã sử dụng.',                 NULL, 'ADMIN'),
    ('wi014', 'Mảnh sứ vỡ',           'INORGANIC', 'Chén bát sứ bị vỡ.',                                   NULL, 'ADMIN'),
    ('wi015', 'Giẻ lau bẩn',          'INORGANIC', 'Giẻ lau, vải vụn không thể tái sử dụng.',             NULL, 'ADMIN'),
    ('wi016', 'Pin cũ',               'HAZARDOUS', 'Pin, linh kiện điện tử chứa chì và hóa chất độc.',     NULL, 'ADMIN'),
    ('wi017', 'Bóng đèn huỳnh quang', 'HAZARDOUS', 'Bóng đèn chứa thủy ngân.',                            NULL, 'ADMIN'),
    ('wi018', 'Thuốc hết hạn',        'HAZARDOUS', 'Thuốc tây, thuốc bổ đã hết hạn sử dụng.',             NULL, 'ADMIN'),
    ('wi019', 'Bình xịt aerosol',     'HAZARDOUS', 'Bình xịt côn trùng, sơn xịt.',                        NULL, 'ADMIN'),
    ('wi020', 'Dầu máy thải',         'HAZARDOUS', 'Dầu nhớt xe máy, ô tô đã qua sử dụng.',              NULL, 'ADMIN');
